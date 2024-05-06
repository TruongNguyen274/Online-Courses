package com.online.learning.controller.back;

import com.online.learning.model.dto.ChapterDTO;
import com.online.learning.model.dto.CourseDTO;
import com.online.learning.model.dto.MessageDTO;
import com.online.learning.model.dto.ResultDTO;
import com.online.learning.model.entity.*;
import com.online.learning.model.mapper.*;
import com.online.learning.service.*;
import com.online.learning.service.custom.CustomUserDetail;
import com.online.learning.validator.ChapterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/back/chapters")
public class ChapterController {

    private static final String REDIRECT_URL = "/back/courses";

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureMapper lectureMapper;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private ResultMapper resultMapper;

    @Autowired
    private ChapterValidator chapterValidator;

    @GetMapping("/form")
    public String create(Model model, Authentication authentication, @RequestParam long courseId) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            Course course = courseService.findById(courseId);
            if (course == null) {
                return "redirect:" + REDIRECT_URL;
            }

            CourseDTO courseDTO = courseMapper.toDTO(course);

            ChapterDTO chapterDTO = new ChapterDTO();
            chapterDTO.setCourseDTO(courseDTO);
            List<Course> courseList = courseService.findByOwner(account);

            model.addAttribute("courseDTO", courseDTO);
            model.addAttribute("chapterDTO", chapterDTO);
            model.addAttribute("courseListDTO", courseMapper.toListDTO(courseList));

            return "back/chapter_form";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/form/{chapterId}")
    public String edit(Model model, @PathVariable long chapterId, Authentication authentication, @RequestParam long courseId,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            Course course = courseService.findById(courseId);
            if (course == null) {
                return "redirect:" + REDIRECT_URL;
            }

            CourseDTO courseDTO = courseMapper.toDTO(course);
            List<Course> courseList = courseService.findByOwner(account);
            Chapter chapter = chapterService.findById(chapterId);

            if (chapter == null) {
                return "redirect:" + REDIRECT_URL;
            }

            List<Lecture> lectureList = lectureService.findByChapter(chapter);
            List<Exam> examList = examService.findByChapter(chapter);
            List<ResultDTO> resultDTOList = resultService.findByChapterId(chapterId);

            model.addAttribute("courseDTO", courseDTO);
            model.addAttribute("chapterDTO", chapterMapper.toDTO(chapter));
            model.addAttribute("courseListDTO", courseMapper.toListDTO(courseList));
            model.addAttribute("lectureListDTO", lectureMapper.toListDTO(lectureList));
            model.addAttribute("examListDTO", examMapper.toListDTO(examList));

            for (int i = 0; i < resultDTOList.size(); i++) {
                ResultDTO resultDTO = resultDTOList.get(i);
                resultDTO.setExamDTO(examMapper.toDTO(examService.findById(resultDTO.getExamId())));
                resultDTO.setAccountDTO(accountMapper.toDTO(accountService.findById(resultDTO.getAccountId())));
                resultDTOList.set(i, resultDTO);
            }

            model.addAttribute("resultDTOList", resultDTOList);

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success")
                                ? "Cập nhật dữ liệu thành công!"
                                : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/chapter_form";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/form")
    public String save(Model model, Authentication authentication, ChapterDTO chapterDTO, BindingResult bindingResult) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            chapterValidator.validate(chapterDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                Chapter chapter = chapterMapper.toEntity(chapterDTO);
                List<Exam> examList = examService.findByChapter(chapter);
                List<Lecture> lectureList = lectureService.findByChapter(chapter);

                List<ResultDTO> resultDTOList = resultService.findByChapterId(chapter.getId());
                Course course = courseService.findById(chapterDTO.getCourseId());
                CourseDTO courseDTO = courseMapper.toDTO(course);
                List<Course> courseList = courseService.findByOwner(account);

                for (int i = 0; i < resultDTOList.size(); i++) {
                    ResultDTO resultDTO = resultDTOList.get(i);
                    resultDTO.setExamDTO(examMapper.toDTO(examService.findById(resultDTO.getExamId())));
                    resultDTO.setAccountDTO(accountMapper.toDTO(accountService.findById(resultDTO.getAccountId())));
                    resultDTOList.set(i, resultDTO);
                }

                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("resultDTOList", resultDTOList);
                model.addAttribute("courseDTO", courseDTO);
                model.addAttribute("courseListDTO", courseMapper.toListDTO(courseList));
                model.addAttribute("examListDTO", examMapper.toListDTO(examList));
                model.addAttribute("chapterDTO", chapterDTO);
                model.addAttribute("lectureListDTO", lectureMapper.toListDTO(lectureList));

                return "back/chapter_form";
            }
            chapterDTO.setOwnerId(account.getId());
            Chapter chapter = chapterService.save(chapterMapper.toEntity(chapterDTO));
            String redirectUrl = "/back/chapters/form/" + chapter.getId() + "?action=save&status=success&courseId=" + chapterDTO.getCourseId();
            return "redirect:" + redirectUrl;
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

}
