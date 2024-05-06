package com.online.learning.controller.back;

import com.online.learning.model.dto.FileDTO;
import com.online.learning.model.dto.MessageDTO;
import com.online.learning.model.dto.QuestionDTO;
import com.online.learning.model.entity.Question;
import com.online.learning.model.mapper.QuestionMapper;
import com.online.learning.service.FileUploadService;
import com.online.learning.service.QuestionService;
import com.online.learning.utils.ConstantUtil;
import com.online.learning.validator.QuestionValidator;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

@Controller
@RequestMapping("/back/question")
public class QuestionController {

    private static final String REDIRECT_URL = "/back/question";

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionValidator questionValidator;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping(value = {"/form/{id}"})
    public String edit(Model model, @PathVariable long id, @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            Question question = questionService.findById(id);
            if (question == null) {
                return "redirect:" + REDIRECT_URL;
            }

            QuestionDTO questionDTO = questionMapper.toDTO(question);
            model.addAttribute("questionDTO", questionDTO);
            model.addAttribute("isQuiz", questionDTO.getExamType().equalsIgnoreCase("Trắc Nghiệm"));
            model.addAttribute("errorList", new HashMap<>());

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/question_form";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping(value = "/form")
    public String save(Model model, QuestionDTO questionDTO, BindingResult bindingResult) {
        try {
             //verify value
            questionValidator.validate(questionDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("questionDTO", questionDTO);
                return "question_form";
            }

            // save
            Question question = questionMapper.toEntity(questionDTO);

            if (questionDTO.getTemplateMul() != null && !ObjectUtils.isEmpty(questionDTO.getTemplateMul().getOriginalFilename())) {
                FileDTO fileDTOBack = fileUploadService.uploadFile(questionDTO.getTemplateMul(), "EXAM");
                question.setAnswer(fileDTOBack.getName());
            }

            questionService.save(question);
            String redirectUrl = "/back/question/form/" + question.getId() + "?action=save&status=success";
            return "redirect:" + redirectUrl;

        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/download/{questionId}")
    public void downloadDocument(@PathVariable long questionId, HttpServletResponse httpServletResponse) throws IOException {
        Question question = questionService.findById(questionId);
        File file = new File(ConstantUtil.RESOURCE_FILE_EXAM_DOWNLOAD + question.getTitle());
        httpServletResponse.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + file.getName();
        httpServletResponse.setHeader(headerKey, headerValue);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
            servletOutputStream.write(buffer, 0, bytesRead);
        }
        bufferedInputStream.close();
        servletOutputStream.close();
    }

}
