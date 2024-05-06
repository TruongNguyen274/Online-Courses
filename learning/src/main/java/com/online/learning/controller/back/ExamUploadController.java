package com.online.learning.controller.back;

import com.online.learning.model.dto.*;
import com.online.learning.model.entity.*;
import com.online.learning.model.mapper.*;
import com.online.learning.service.*;
import com.online.learning.service.custom.CustomUserDetail;
import com.online.learning.validator.ExamValidator;
import com.online.learning.validator.QuestionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/back/exam/upload")
public class ExamUploadController {

    private static final String REDIRECT_URL = "/back/exam/upload";

    private static final String NAME_TEMPLATE = "Exam_Template.xlsx";

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

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
    private ReadFileExamService readFileExamService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ExamValidator examValidator;

    @Autowired
    private QuestionValidator questionValidator;

    @GetMapping("/form")
    public String create(Model model,
                         @RequestParam(required = false, defaultValue = "0") long courseId,
                         @RequestParam(required = false, defaultValue = "0") long chapterId,
                         @RequestParam(required = false, defaultValue = "0") long lectureId,
                         @RequestParam(required = false) String action,
                         @RequestParam(required = false) String status) {
        try {
            ExamDTO examDTO = createDefault(courseId, chapterId, lectureId);

            model.addAttribute("examDTO", examDTO);
            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success")
                                ? "Cập nhật dữ liệu thành công!"
                                : "Vui lòng kiểm tra lại tài liệu!"));
            }

            return "back/exam_create";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/form")
    public String upload(Model model, ExamDTO examDTO, BindingResult bindingResultExam) {
        try {
            FileDTO fileDTOBack = fileUploadService.uploadQuizExamFile(examDTO.getTemplateMul());
            if (!fileDTOBack.getName().equalsIgnoreCase(NAME_TEMPLATE)) {
                return uploadFailed(model, examDTO);
            }

            examDTO.setPathFileExcel(fileDTOBack.getPath());
            examDTO = readFileExamService.readQuiz(examDTO);

            if (examDTO.getQuestionDTOList() == null) {
                return uploadFailed(model, examDTO);
            }

            examValidator.validate(examDTO, bindingResultExam);
            if (bindingResultExam.hasErrors()) {
                return uploadFailed(model, examDTO);
            }

            Exam exam = examMapper.toEntity(examDTO);
            examService.save(exam);

            for (QuestionDTO questionDTO : examDTO.getQuestionDTOList()) {
                questionDTO.setExamDTO(examDTO);
                questionDTO.setExamId(exam.getId());
                questionDTO.setStatus(true);
                Question question = questionMapper.toEntity(questionDTO);
                questionService.save(question);
            }

            return "redirect:/back/exam/form/" + exam.getId() + "?action=save&status=success";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    /* Create exam by essay
     * */
    @GetMapping("/form/essay")
    public String createEssay(Model model, Authentication authentication,
                              @RequestParam(required = false, defaultValue = "0") long courseId,
                              @RequestParam(required = false, defaultValue = "0") long chapterId,
                              @RequestParam(required = false, defaultValue = "0") long lectureId,
                              @RequestParam(required = false) String action,
                              @RequestParam(required = false) String status) {
        try {
            ExamDTO examDTO = createDefaultExamEssay(courseId, chapterId, lectureId);

            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            List<Course> courseList = courseService.findByOwner(account);
            List<Chapter> chapterList = chapterService.findByOwnerId(account.getId());
            List<Lecture> lectureList = lectureService.findByOwnerId(account.getId());

            model.addAttribute("examDTO", examDTO);
            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success")
                                ? "Cập nhật dữ liệu thành công!"
                                : "Vui lòng kiểm tra lại tài liệu!"));
            }

            model.addAttribute("courseDTOList", courseMapper.toListDTO(courseList));
            model.addAttribute("chapterDTOList", chapterMapper.toListDTO(chapterList));
            model.addAttribute("lectureDTOList", lectureMapper.toListDTO(lectureList));

            return "back/exam_essay";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/form/essay")
    public String saveEssay(Model model, ExamDTO examDTO, BindingResult bindingResultExam) {
        try {
            FileDTO fileDTOBack = fileUploadService.uploadFile(examDTO.getTemplateMul(), "EXAM");
            examDTO.setPathFileExcel(fileDTOBack.getName());
            examDTO.setQuestionDTOList(saveQuestionDTOByExamEssay(fileDTOBack));

            examValidator.validate(examDTO, bindingResultExam);
            if (bindingResultExam.hasErrors()) {
                return uploadFailed(model, examDTO);
            }

            Exam exam = examMapper.toEntity(examDTO);
            examService.save(exam);

            for (QuestionDTO questionDTO : examDTO.getQuestionDTOList()) {
                questionDTO.setExamDTO(examDTO);
                questionDTO.setExamId(exam.getId());
                questionDTO.setStatus(true);
                Question question = questionMapper.toEntity(questionDTO);
                questionService.save(question);
            }

            return "redirect:/back/exam/form/" + exam.getId() + "?action=save&status=success";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    private ExamDTO createDefault(long courseId, long chapterId, long lectureId) {
        ExamDTO examDTO = new ExamDTO();
        Course course = courseService.findById(courseId);
        Chapter chapter = chapterService.findById(chapterId);
        Lecture lecture = lectureService.findById(lectureId);

        examDTO.setCourseId(courseId);
        if (course != null) {
            examDTO.setCourseDTO(courseMapper.toDTO(course));
        }
        examDTO.setChapterId(chapterId);
        if (chapter != null) {
            examDTO.setChapterDTO(chapterMapper.toDTO(chapter));
        }
        examDTO.setLectureId(lectureId);
        if (lecture != null) {
            examDTO.setLectureDTO(lectureMapper.toDTO(lecture));
        }
        examDTO.setStatus(true);
        return examDTO;
    }

    private ExamDTO createDefaultExamEssay(long courseId, long chapterId, long lectureId) {
        ExamDTO examDTO = new ExamDTO();
        Course course = courseService.findById(courseId);
        Chapter chapter = chapterService.findById(chapterId);
        Lecture lecture = lectureService.findById(lectureId);

        examDTO.setCourseId(courseId);
        if (course != null) {
            examDTO.setCourseDTO(courseMapper.toDTO(course));
            examDTO.setKindExam("Khóa Học");
        }
        examDTO.setChapterId(chapterId);
        if (chapter != null) {
            examDTO.setChapterDTO(chapterMapper.toDTO(chapter));
            examDTO.setKindExam("Chương");
        }
        examDTO.setLectureId(lectureId);
        if (lecture != null) {
            examDTO.setLectureDTO(lectureMapper.toDTO(lecture));
            examDTO.setKindExam("Bài Học");
        }
        examDTO.setType("Tự Luận");
        examDTO.setStatus(true);

        return examDTO;
    }

    private String uploadFailed(Model model, ExamDTO examDTO) {
        CourseDTO courseDTO = courseMapper.toDTO(courseService.findById(examDTO.getCourseId()));
        ChapterDTO chapterDTO = chapterMapper.toDTO(chapterService.findById(examDTO.getChapterId()));
        LectureDTO lectureDTO = lectureMapper.toDTO(lectureService.findById(examDTO.getLectureId()));

        examDTO.setCourseDTO(courseDTO);
        examDTO.setChapterDTO(chapterDTO);
        examDTO.setLectureDTO(lectureDTO);

        model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                "Vui lòng kiểm tra lại thông tin!"));
        model.addAttribute("examDTO", examDTO);
        return "back/exam_create";
    }

    private List<QuestionDTO> saveQuestionDTOByExamEssay(FileDTO fileDTOBack ) {
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setTitle(fileDTOBack.getName());
        questionDTO.setAnswer(fileDTOBack.getFileName());
        questionDTO.setStatus(true);

        questionDTOList.add(questionDTO);
        return questionDTOList;
    }

}
