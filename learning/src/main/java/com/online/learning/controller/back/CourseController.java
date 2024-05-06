package com.online.learning.controller.back;

import com.online.learning.model.dto.CourseDTO;
import com.online.learning.model.dto.FileDTO;
import com.online.learning.model.dto.MessageDTO;
import com.online.learning.model.dto.ResultDTO;
import com.online.learning.model.entity.*;
import com.online.learning.model.mapper.*;
import com.online.learning.service.*;
import com.online.learning.service.custom.CustomUserDetail;
import com.online.learning.utils.FormatUtils;
import com.online.learning.validator.CourseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/back/courses")
public class CourseController {

    private static final String REDIRECT_URL = "/back/courses";

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CourseValidator courseValidator;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ResultService resultService;

    @Autowired
    private ResultMapper resultMapper;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping(value = {"/", ""})
    public String list(Model model, Authentication authentication) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            List<Course> courseList;
            if (account.getRole().equalsIgnoreCase("ADMIN")) {
                courseList = courseService.findAll();
            } else {
                courseList = courseService.findByOwner(account);
            }
            model.addAttribute("courseListDTO", courseMapper.toListDTO(courseList));

            return "back/course_list";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping(value = "/form")
    public String create(Model model, Authentication authentication) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();

            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setOwnerId(account.getId());

            List<Category> categoryList = categoryService.findAll();
            List<Account> accountList = accountService.findAll();

            model.addAttribute("courseDTO", courseDTO);
            model.addAttribute("accountList", accountMapper.toListDTO(accountList));
            model.addAttribute("categoryListDTO", categoryMapper.toListDTO(categoryList));

            return "back/course_form";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping(value = "/form/{courseId}")
    public String edit(Model model, @PathVariable long courseId,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            Course course = courseService.findById(courseId);
            if (course == null) {
                return "redirect:" + REDIRECT_URL;
            }

            List<Account> accountList = accountService.findAll();
            List<Category> categoryList = categoryService.findAll();
            List<Chapter> chapterList = chapterService.findByCourse(course);
            List<Exam> examList = examService.findByCourseId(courseId);
            List<ResultDTO> resultDTOList = resultService.findByCourseId(courseId);

            model.addAttribute("courseDTO", courseMapper.toDTO(course));
            model.addAttribute("chapterListDTO", chapterMapper.toListDTO(chapterList));
            model.addAttribute("categoryListDTO", categoryMapper.toListDTO(categoryList));
            model.addAttribute("examListDTO", examMapper.toListDTO(examList));
            model.addAttribute("accountList", accountMapper.toListDTO(accountList));

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

            return "back/course_form";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping(value = "/form")
    public String save(Model model, CourseDTO courseDTO, BindingResult bindingResult) {
        try {
            // format data
            courseDTO.setPrice(FormatUtils.toEncodePrice(courseDTO.getPrice()));
            courseDTO.setDiscount(FormatUtils.toEncodePrice(courseDTO.getDiscount()));

            // validator
            courseValidator.validate(courseDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                Course course = new Course();
                course.setId(courseDTO.getId());
                List<Exam> examList = examService.findByCourseId(courseDTO.getId());
                List<Chapter> chapterList = chapterService.findByCourse(course);
                List<ResultDTO> resultDTOList = resultService.findByCourseId(courseDTO.getId());

                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("categoryListDTO", categoryService.findAll());
                model.addAttribute("chapterListDTO", chapterMapper.toListDTO(chapterList));
                model.addAttribute("accountList", accountService.findAll());
                model.addAttribute("examListDTO", examMapper.toListDTO(examList));
                model.addAttribute("courseDTO", courseDTO);

                for (int i = 0; i < resultDTOList.size(); i++) {
                    ResultDTO resultDTO = resultDTOList.get(i);
                    resultDTO.setExamDTO(examMapper.toDTO(examService.findById(resultDTO.getExamId())));
                    resultDTO.setAccountDTO(accountMapper.toDTO(accountService.findById(resultDTO.getAccountId())));
                    resultDTOList.set(i, resultDTO);
                }

                model.addAttribute("resultDTOList", resultDTOList);

                return "back/course_form";
            }
            Course course = courseMapper.toEntity(courseDTO);

            if (courseDTO.getAvatarMul() != null && !ObjectUtils.isEmpty(courseDTO.getAvatarMul().getOriginalFilename())) {
                FileDTO fileDTOBack = fileUploadService.uploadFile(courseDTO.getAvatarMul(), "IMAGE");
                course.setAvatar(fileDTOBack.getPath());
            }

            courseService.save(course);

            String redirectUrl = "/back/courses/form/" + course.getId() + "?action=save&status=success";
            return "redirect:" + redirectUrl;
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

}
