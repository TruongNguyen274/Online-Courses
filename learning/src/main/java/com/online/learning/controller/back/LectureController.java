package com.online.learning.controller.back;

import com.online.learning.model.dto.*;
import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Chapter;
import com.online.learning.model.entity.Exam;
import com.online.learning.model.entity.Lecture;
import com.online.learning.model.mapper.*;
import com.online.learning.service.*;
import com.online.learning.service.custom.CustomUserDetail;
import com.online.learning.validator.LectureValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/back/lectures")
public class LectureController {

    private static final String REDIRECT_URL = "/back/lectures";

    @Autowired
    private LectureService lectureService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private LectureMapper lectureMapper;

    @Autowired
    private ChapterMapper chapterMapper;

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
    private LectureValidator lectureValidator;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping(value = "/form")
    public String create(Model model, Authentication authentication, @RequestParam long chapterId) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            Chapter chapter = chapterService.findById(chapterId);
            if (chapter == null) {
                return "redirect:" + REDIRECT_URL;
            }

            ChapterDTO chapterDTO = chapterMapper.toDTO(chapter);
            LectureDTO lectureDTO = new LectureDTO();
            lectureDTO.setChapterDTO(chapterDTO);
            lectureDTO.setChapterId(chapterDTO.getId());
            List<Chapter> chapterList = chapterService.findByOwnerId(account.getId());

            model.addAttribute("chapterDTO", chapterDTO);
            model.addAttribute("lectureDTO", lectureDTO);
            model.addAttribute("chapterListDTO", chapterMapper.toListDTO(chapterList));

            return "back/lecture_form";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping(value = "/form/{lectureId}")
    public String edit(Model model, Authentication authentication, @PathVariable long lectureId, @RequestParam long chapterId,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            Chapter chapter = chapterService.findById(chapterId);
            if (chapter == null) {
                return "redirect:" + REDIRECT_URL;
            }

            ChapterDTO chapterDTO = chapterMapper.toDTO(chapter);

            List<Chapter> chapterList = chapterService.findByOwnerId(account.getId());
            Lecture lecture = lectureService.findById(lectureId);
            LectureDTO lectureDTO = lectureMapper.toDTO(lecture);
            lectureDTO.setChapterDTO(chapterDTO);
            lectureDTO.setChapterId(chapterDTO.getId());
            if (lecture == null) {
                return "redirect:" + REDIRECT_URL;
            }

            List<Exam> examList = examService.findByLecture(lecture);
            List<ResultDTO> resultDTOList = resultService.findByLectureId(lectureId);

            model.addAttribute("chapterDTO", chapterDTO);
            model.addAttribute("lectureDTO", lectureDTO);
            model.addAttribute("chapterListDTO", chapterMapper.toListDTO(chapterList));
            model.addAttribute("examListDTO", examMapper.toListDTO(examList));
            model.addAttribute("errorList", new HashMap<>());

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

            return "back/lecture_form";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping(value = "/form")
    public String save(Model model, Authentication authentication, LectureDTO lectureDTO, BindingResult bindingResult) {
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetail.getAccount();
            Chapter chapter = chapterService.findById(lectureDTO.getChapterId());
            if (chapter == null) {
                return "redirect:" + REDIRECT_URL;
            }

            lectureValidator.validate(lectureDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                Lecture lecture = lectureMapper.toEntity(lectureDTO);
                List<Exam> examList = examService.findByLecture(lecture);
                ChapterDTO chapterDTO = chapterMapper.toDTO(chapter);
                List<ResultDTO> resultDTOList = resultService.findByLectureId(lecture.getId());

                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("chapterDTO", chapterDTO);
                model.addAttribute("lectureDTO", lectureDTO);
                model.addAttribute("chapterListDTO", chapterService.findByOwnerId(account.getId()));
                model.addAttribute("examListDTO", examMapper.toListDTO(examList));


                for (int i = 0; i < resultDTOList.size(); i++) {
                    ResultDTO resultDTO = resultDTOList.get(i);
                    resultDTO.setExamDTO(examMapper.toDTO(examService.findById(resultDTO.getExamId())));
                    resultDTO.setAccountDTO(accountMapper.toDTO(accountService.findById(resultDTO.getAccountId())));
                    resultDTOList.set(i, resultDTO);
                }

                model.addAttribute("resultDTOList", resultDTOList);
                return "back/lecture_form";
            }
            lectureDTO.setOwnerId(account.getId());
            Lecture lecture = lectureMapper.toEntity(lectureDTO);

            if (lectureDTO.getVideoMul() != null && !ObjectUtils.isEmpty(lectureDTO.getVideoMul().getOriginalFilename())) {
                FileDTO fileDTOBack = fileUploadService.uploadFile(lectureDTO.getVideoMul(), "VIDEO");
                lecture.setLinkVideo(fileDTOBack.getPath());
            }

            if (lectureDTO.getResourceMul() != null && !ObjectUtils.isEmpty(lectureDTO.getResourceMul().getOriginalFilename())) {
                FileDTO fileDTOBack = fileUploadService.uploadFile(lectureDTO.getResourceMul(), "DOCUMENT");
                lecture.setLinkResource(fileDTOBack.getPath());
            }
            lectureService.save(lecture);

            String redirectUrl = "/back/lectures/form/" + lecture.getId() + "?action=save&status=success&chapterId=" + lectureDTO.getChapterId();
            return "redirect:" + redirectUrl;
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

}
