package com.online.learning.controller.back;

import com.online.learning.model.dto.ExamDTO;
import com.online.learning.model.dto.MessageDTO;
import com.online.learning.model.dto.QuestionDTO;
import com.online.learning.model.entity.*;
import com.online.learning.model.mapper.*;
import com.online.learning.service.*;
import com.online.learning.service.custom.CustomUserDetail;
import com.online.learning.utils.ConstantUtil;
import com.online.learning.validator.ExamValidator;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/back/exam")
public class ExamController {

    private static final String REDIRECT_URL = "/back/courses";

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private LectureMapper lectureMapper;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;


    @Autowired
    private ReadFileExamService readFileExamService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ExamValidator examValidator;

    @GetMapping("/form/{examId}")
    public String view(Model model, @PathVariable long examId, Authentication authentication,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            Exam exam = examService.findById(examId);
            List<Course> courseList = courseService.findByOwner(account);
            List<Chapter> chapterList = chapterService.findByOwnerId(account.getId());
            List<Lecture> lectureList = lectureService.findByOwnerId(account.getId());
            List<Question> questionList = questionService.findByExam(exam);

            model.addAttribute("examDTO", examMapper.toDTO(exam));
            model.addAttribute("courseDTOList", courseMapper.toListDTO(courseList));
            model.addAttribute("chapterDTOList", chapterMapper.toListDTO(chapterList));
            model.addAttribute("lectureDTOList", lectureMapper.toListDTO(lectureList));
            model.addAttribute("questionDTOList", questionMapper.toListDTO(questionList));

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success")
                                ? "Cập nhật dữ liệu thành công!"
                                : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/exam_form";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/form")
    public String save(Model model, ExamDTO examDTO, Authentication authentication, BindingResult bindingResult) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();

            List<Question> questionList = questionService.findByExam(examMapper.toEntity(examDTO));
            List<QuestionDTO>  questionDTOList = questionMapper.toListDTO(questionList);
            examDTO.setQuestionDTOList(questionDTOList);

            examValidator.validate(examDTO, bindingResult);
            if (bindingResult.hasErrors()) {
                List<Course> courseList = courseService.findByOwner(account);
                List<Chapter> chapterList = chapterService.findByOwnerId(account.getId());
                List<Lecture> lectureList = lectureService.findByOwnerId(account.getId());

                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("courseDTOList", courseMapper.toListDTO(courseList));
                model.addAttribute("chapterDTOList", chapterMapper.toListDTO(chapterList));
                model.addAttribute("lectureDTOList", lectureMapper.toListDTO(lectureList));
                model.addAttribute("questionDTOList", questionDTOList);
                model.addAttribute("examDTO", examDTO);

                return "back/exam_form";
            }

            Exam exam = examMapper.toEntity(examDTO);
            examService.save(exam);

            return "redirect:form/" + exam.getId() + "?action=save&status=success";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/download")
    public void downloadFile(HttpServletResponse httpServletResponse) throws IOException {
        File file = new File(ConstantUtil.FILE_DOWNLOAD);
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
