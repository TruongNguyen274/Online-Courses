package com.online.learning.controller.front;

import com.online.learning.model.dto.ChapterDTO;
import com.online.learning.model.dto.ExamDTO;
import com.online.learning.model.dto.LectureDTO;
import com.online.learning.model.entity.*;
import com.online.learning.model.mapper.*;
import com.online.learning.service.*;
import com.online.learning.service.custom.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseLearnController {

    private static final int FIRST_INDEX = 0;

    private static final String EXAM_PASSED = "PASSED";

    @Autowired
    private CourseService courseService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private ExamService examService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private LectureMapper lectureMapper;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ResultMapper resultMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("/learn")
    public String learnCourseByTitle(Model model, Authentication authentication, @RequestParam long courseId,
                                     @RequestParam(required = false) String lectureId) {
        if (authentication == null) {
            return "redirect:/login";
        }

        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Account account = customUserDetail.getAccount();

        Course course = courseService.findById(courseId);
        if (course == null) {
            return "front/403_error";
        }

        boolean isFound = isCourseInAccount(course, account);
        if (!isFound) {
            return "front/403_error";
        }

        List<Chapter> chapterList = chapterService.findByCourse(course);
        List<ChapterDTO> chapterDTOList = chapterMapper.toListDTO(chapterList);
        List<Exam> examListByLecture = new ArrayList<>();
        List<Result> resultListByLecture = new ArrayList<>();
        List<ExamDTO> examDTOListByCourse = new ArrayList<>();
        List<Comment> commentList = new ArrayList<>();
        Lecture lecture = new Lecture();

        if (!chapterDTOList.isEmpty()) {
            for (int index = 0; index < chapterDTOList.size(); index++) {

                ChapterDTO chapterDTO = chapterDTOList.get(index);
                Chapter chapter = new Chapter();
                chapter.setId(chapterDTO.getId());

                if (index == FIRST_INDEX) {
                    chapterDTO.setExamPassed(true);
                }

                // get list lecture by chapter
                // and get list exam by lecture
                loadLectureListByChapter(account, chapterDTO);

                List<Exam> examListByChapter = examService.findByChapter(chapter);
                int nextIndex = index + 1;
                if (examListByChapter.isEmpty() && nextIndex < chapterDTOList.size()) {
                    ChapterDTO nextChapterDTO = chapterDTOList.get(nextIndex);
                    nextChapterDTO.setExamPassed(true);
                    chapterDTOList.set(nextIndex, nextChapterDTO);
                } else {
                    setExamPassedByChapter(index, account, examListByChapter, chapterDTOList);
                }

                // get list exam by chapter
                chapterDTO.setExamDTOList(loadExamDTOListByChapter(account, examListByChapter, chapterDTO));

                if (index == 0) {
                    chapterDTO.setExamPassed(true);
                }

                chapterDTOList.set(index, chapterDTO);
            }

            if (lectureId != null) {
                lecture = lectureService.findById(Long.parseLong(lectureId));
            } else {
                lecture = lectureService.findById(chapterDTOList.get(FIRST_INDEX).getLectureList().get(FIRST_INDEX).getId());
            }

            examListByLecture = examService.findByLecture(lecture);
            resultListByLecture = findResultByExam(account, examListByLecture);

            ChapterDTO lastChapter = chapterDTOList.get(chapterDTOList.size() - 1);
            examDTOListByCourse = loadExamDTOListByCourse(account, courseId, lastChapter);
            commentList = commentService.findByLecture(lecture);

            examListByLecture = removeCompletedExam(account, lecture);
        }

        model.addAttribute("courseDTO", courseMapper.toDTO(course));
        model.addAttribute("chapterDTOList", chapterDTOList);
        model.addAttribute("lectureDTO", lectureMapper.toDTO(lecture));
        model.addAttribute("examDTOListByLecture", examMapper.toListDTO(examListByLecture));
        model.addAttribute("examDTOListByCourse", examDTOListByCourse);
        model.addAttribute("resultDTOList", resultMapper.toListDTO(resultListByLecture));
        model.addAttribute("commentDTOList", commentMapper.toListDTO(commentList));

        return "front/course_learn";
    }

    private boolean isCourseInAccount(Course course, Account account) {
        return orderService.findCourseInAccount(course, account);
    }

    private List<Result> findResultByExam(Account account, List<Exam> examList) {
        List<Result> results = new ArrayList<>();

        for (Exam exam : examList) {
            List<Result> resultList = resultService.findByAccountAndExam(account, exam);
            if (!resultList.isEmpty()) {
//                Result firstResult = resultList.get(FIRST_INDEX);
                results.addAll(resultList);
            }
        }

        return results;
    }

    private List<Exam> removeCompletedExam(Account account, Lecture lecture) {
        List<Exam> examListByLecture = examService.findByLecture(lecture);
        List<Exam> result = new ArrayList<>();

        for (Exam exam : examListByLecture) {
            List<Result> resultList = resultService.findByAccountAndExam(account, exam);
            if (resultList.isEmpty() || !resultList.get(FIRST_INDEX).getResult().equals(EXAM_PASSED)) {
                result.add(exam);
            }
        }

        return result;
    }

    private List<ExamDTO> loadExamDTOListByChapter(Account account, List<Exam> examListByChapter, ChapterDTO chapterDTO) {
        List<ExamDTO> examDTOList = examMapper.toListDTO(examListByChapter);

        if (examDTOList.isEmpty()) {
            chapterDTO.setExamPassed(true);
        }

        for (int index = FIRST_INDEX; index < examDTOList.size(); index++) {
            ExamDTO examDTO = examDTOList.get(index);
            Exam exam = examListByChapter.get(index);
            int nextIndex = index + 1;
            List<Result> resultList = resultService.findByAccountAndExam(account, exam);

            // If the chapter have an exam list.
            // Check if current exam by chapter had a passed exam, the next exam by chapter also has a passed exam.
            if (nextIndex <= examDTOList.size() - 1 && !resultList.isEmpty()) {
                Result result = resultList.get(FIRST_INDEX);

                examDTO.setExamPassed(result.getResult().equals(EXAM_PASSED));
                examDTO.setResult(result.getResult());

                ExamDTO nextExamDTO = examDTOList.get(nextIndex);
                List<Result> nextResultDTOList = resultService.findByAccountAndExam(account, examMapper.toEntity(nextExamDTO));
                if (!nextResultDTOList.isEmpty()) {
                    Result nextResultDTO = nextResultDTOList.get(FIRST_INDEX);
                    if (nextResultDTO.getResult().equals(EXAM_PASSED)) {
                        nextExamDTO.setResult(EXAM_PASSED);
                    }
                }
                nextExamDTO.setExamPassed(result.getResult().equals(EXAM_PASSED));
                examDTOList.set(nextIndex, nextExamDTO);
            }

            // Check last chapter and last lesson has passed the exam?
            if (index == FIRST_INDEX) {
                List<LectureDTO> lectureDTOList = chapterDTO.getLectureList();
                if (!lectureDTOList.isEmpty()) {
                    LectureDTO lastLectureDTO = lectureDTOList.get(lectureDTOList.size() - 1);
                    if (lastLectureDTO.isExamPassed()) {
                        examDTO.setExamPassed(true);
                    }
                }
            }
        }

        return examDTOList;
    }

    private List<ExamDTO> loadExamDTOListByCourse(Account account, long courseId, ChapterDTO chapterDTO) {
        List<Exam> examList = examService.findByCourseId(courseId);
        List<ExamDTO> examDTOList = examMapper.toListDTO(examList);

        if (!examList.isEmpty()) {
            for (int index = FIRST_INDEX; index < examList.size(); index++) {
                ExamDTO examDTO = examDTOList.get(index);
                Exam exam = examList.get(index);
                int nextIndex = index + 1;

                // Check last chapter and last lesson has passed the exam?
                if (index == FIRST_INDEX) {
                    List<LectureDTO> lectureDTOList = chapterDTO.getLectureList();
                    if (!lectureDTOList.isEmpty()) {
                        LectureDTO lastLectureDTO = lectureDTOList.get(lectureDTOList.size() - 1);
                        if (lastLectureDTO.isExamPassed() && chapterDTO.isExamPassed()) {
                            examDTO.setExamPassed(true);
                            examDTOList.set(index, examDTO);
                        }
                    }
                }

                // Get list result by account and exam
                // If result not empty
                // Check new result of first exam, if result has passed, set next exam has passed
                List<Result> resultList = resultService.findByAccountAndExam(account, exam);
                if (nextIndex <= examDTOList.size() - 1 && !resultList.isEmpty()) {
                    Result result = resultList.get(FIRST_INDEX);

                    examDTO.setExamPassed(result.getResult().equals(EXAM_PASSED));
                    examDTO.setResult(result.getResult());

                    ExamDTO nextExamDTO = examDTOList.get(nextIndex);
                    nextExamDTO.setExamPassed(result.getResult().equals(EXAM_PASSED));
                    examDTOList.set(nextIndex, nextExamDTO);
                }

                if (index == examList.size() - 1 && !resultList.isEmpty()) {
                    Result result = resultList.get(FIRST_INDEX);

                    if (result.getResult().equals(EXAM_PASSED)) {
                        examDTO.setExamPassed(true);
                    }
                    examDTO.setResult(result.getResult());
                }
            }

        }

        return examDTOList;
    }

    private void setExamPassedByChapter(int index, Account account, List<Exam> examList, List<ChapterDTO> chapterDTOList) {
        // Get list result by chapter
        // Check new result of last exam. If result has passed, set next chapter has passed.
        if (!examList.isEmpty()) {
            Exam lastExam = examList.get(examList.size() - 1);
            List<Result> resultList = resultService.findByAccountAndExam(account, lastExam);
            int nextIndex = index + 1;
            if (nextIndex < chapterDTOList.size() && !resultList.isEmpty()) {
                Result result = resultList.get(FIRST_INDEX);
                ChapterDTO nextChapterDTO = chapterDTOList.get(nextIndex);
                nextChapterDTO.setExamPassed(result.getResult().equals(EXAM_PASSED));
                chapterDTOList.set(nextIndex, nextChapterDTO);
            }
        }
    }

    private void loadLectureListByChapter(Account account, ChapterDTO chapterDTO) {
        Chapter chapter = new Chapter();
        chapter.setId(chapterDTO.getId());
        List<LectureDTO> lectureDTOList = lectureMapper.toListDTO(lectureService.findByChapter(chapter));

        for (int indexLectureDTO = FIRST_INDEX; indexLectureDTO < lectureDTOList.size(); indexLectureDTO++) {
            LectureDTO lectureDTO = lectureDTOList.get(indexLectureDTO);
            List<Exam> examList = examService.findByLecture(lectureMapper.toEntity(lectureDTO));

            // Set first lecture of chapter has passed the exam
            if (indexLectureDTO == FIRST_INDEX && chapterDTO.isExamPassed()) {
                lectureDTO.setExamPassed(true);
                lectureDTOList.set(indexLectureDTO, lectureDTO);
            }

            // If the lecture does not have an exam list
            // And it is not first element
            // -> Check if previous lecture had a passed exam, the current lecture also has a passed exam.
            if (examList.isEmpty() && indexLectureDTO > 0) {
                LectureDTO preLectureDTO = lectureDTOList.get(indexLectureDTO - 1);
                lectureDTO.setExamPassed(preLectureDTO.isExamPassed());
                lectureDTOList.set(indexLectureDTO, lectureDTO);
            }

            // If the lecture have an exam list
            // Check new result of first exam. If result has passed, set next lecture has passed.
            if (!examList.isEmpty()) {
                int nextIndex = indexLectureDTO + 1;
                List<Result> resultList = resultService.findByAccountAndExam(account, examList.get(FIRST_INDEX));
                LectureDTO nextLectureDTO = lectureDTOList.get(nextIndex);

                if (nextIndex <= lectureDTOList.size() - 1 && !resultList.isEmpty()) {
                    Result result = resultList.get(FIRST_INDEX);
                    nextLectureDTO.setExamPassed(result.getResult().equals(EXAM_PASSED));
                    lectureDTOList.set(nextIndex, nextLectureDTO);
                }

            }

        }

        chapterDTO.setLectureList(lectureDTOList);
    }

}
