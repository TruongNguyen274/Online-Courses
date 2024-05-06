package com.online.learning.controller.back;

import com.online.learning.model.dto.MessageDTO;
import com.online.learning.model.dto.ResultDTO;
import com.online.learning.model.entity.Answer;
import com.online.learning.model.entity.Result;
import com.online.learning.model.mapper.AnswerMapper;
import com.online.learning.model.mapper.ResultMapper;
import com.online.learning.service.AnswerService;
import com.online.learning.service.ResultService;
import com.online.learning.utils.ConstantUtil;
import com.online.learning.validator.ResultValidator;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/back/result")
public class ResultController {

    private static final String REDIRECT_URL = "/back/result";

    private static final double SCORE_PASSED = 5.0;

    @Autowired
    private ResultService resultService;

    @Autowired
    private ResultMapper resultMapper;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private ResultValidator resultValidator;

    @GetMapping("/form/{resultId}")
    public String edit(Model model, @PathVariable long resultId,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            Result result = resultService.findById(resultId);
            ResultDTO resultDTO = resultMapper.toDTO(result);
            List<Answer> answerList = answerService.findByResult(result);
            Date startDate = result.getStartTime();
            Date endDate = result.getStartTime();

            model.addAttribute("resultDTO", resultDTO);
            model.addAttribute("startDate", startDate.getTime());
            model.addAttribute("endDate", endDate.getTime());
            model.addAttribute("answerDTOList", answerMapper.toListDTO(answerList));
            model.addAttribute("isQuiz", resultDTO.getExamDTO().getType().equalsIgnoreCase("Trắc Nghiệm"));

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/result_form";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/form")
    public String save(Model model, ResultDTO resultDTO, long startDate, long endDate, BindingResult bindingResult) {
        try {
            resultValidator.validate(resultDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                Result result = new Result();
                result.setId(resultDTO.getId());
                List<Answer> answerList = answerService.findByResult(result);

                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("resultDTO", resultDTO);
                model.addAttribute("answerDTOList", answerMapper.toListDTO(answerList));

                return "back/result_form";
            }

            if (resultDTO.getScore() >= SCORE_PASSED) {
                resultDTO.setResult("PASSED");
            } else {
                resultDTO.setResult("FAILED");
            }

            Result result = resultMapper.toEntity(resultDTO);
            result.setStartTime(new Date(startDate));
            result.setEndTime(new Date(endDate));
            resultService.save(result);

            String redirectUrl = "/back/result/form/" + result.getId() + "?action=save&status=success";
            return "redirect:" + redirectUrl;
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/download/{answerId}")
    public void downloadDocument(@PathVariable long answerId, HttpServletResponse httpServletResponse) throws IOException {
        Answer answer = answerService.findById(answerId);
        File file = new File(ConstantUtil.RESOURCE_FILE_RESULT_DOWNLOAD + answer.getChoose());
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
