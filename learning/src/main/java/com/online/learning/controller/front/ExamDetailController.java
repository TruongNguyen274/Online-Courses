package com.online.learning.controller.front;

import com.online.learning.model.dto.FileDTO;
import com.online.learning.model.dto.QuestionDTO;
import com.online.learning.model.entity.*;
import com.online.learning.model.mapper.ExamMapper;
import com.online.learning.model.mapper.QuestionMapper;
import com.online.learning.model.mapper.ResultMapper;
import com.online.learning.service.*;
import com.online.learning.service.custom.CustomUserDetail;
import com.online.learning.utils.ConstantUtil;
import com.online.learning.utils.FormatUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/exam")
public class ExamDetailController {

    private static final double SCORE_PASSED = 5.0;

    private static final int INDEX_FIRST_ELEMENT = 0;

    private static final String EXAM_TYPE_QUIZ = "Trắc Nghiệm";

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ResultService resultService;

    @Autowired
    private ResultMapper resultMapper;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/{examId}")
    public String detail(Model model, @RequestParam long courseId, Authentication authentication, @PathVariable long examId) {
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Account account = customUserDetail.getAccount();

        Date date = new Date();
        Exam exam = examService.findById(examId);
        List<Question> questionList = questionService.findByExam(exam);
        List<Result> resultList = resultService.findByAccountAndExam(account, exam);
        model.addAttribute("courseId", courseId);
        model.addAttribute("examDTO", examMapper.toDTO(exam));

        if (!resultList.isEmpty()) {
            Result firstResult = resultList.get(INDEX_FIRST_ELEMENT);
            if (firstResult.getResult().equals("PASSED")) {
                model.addAttribute("resultDTO", resultMapper.toDTO(firstResult));
                return "front/exam_success";
            }
        }

        List<Result> resultListByDate = resultService.findByAccountAndExamAndDate(account, exam, date);
        int numberOfDay = Integer.parseInt(exam.getNumberOfDay().substring(0, 1));
        if (isNumberOfDayLimit(numberOfDay, resultListByDate.size())) {
            model.addAttribute("message", "Bạn đã hết số lần làm bài kiểm tra! Vui lòng quay lại vào ngày hôm sau.");
            return "front/exam_failed";
        }

        model.addAttribute("dateStart", date.getTime());
        model.addAttribute("questionDTOList", questionMapper.toListDTO(questionList));

        if (exam.getType().equals(EXAM_TYPE_QUIZ)) {
            return "front/exam_quiz";
        } else {
            return "front/exam_essay";
        }
    }

    @PostMapping("/quiz/submit")
    public String saveQuiz(Model model, Authentication authentication,
                       @RequestParam Map<String, String> requestParams,
                       @RequestParam long examId, @RequestParam long courseId, long dateStart) {
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Account account = customUserDetail.getAccount();
        Date date = new Date();
        int examRequest = requestParams.size() - 1;
        int timeRequest = requestParams.size() - 2;
        int courseIdRequest = requestParams.size() - 3;

        Exam exam = examService.findById(examId);
        List<QuestionDTO> questionDTOList = questionMapper.toListDTO(questionService.findByExam(exam));
        List<Answer> answerList = new ArrayList<>();
        List<Result> resultList = resultService.findByAccountAndExamAndDate(account, exam, date);

        int numberOfDay = Integer.parseInt(exam.getNumberOfDay().substring(0, 1));
        if (isNumberOfDayLimit(numberOfDay, resultList.size())) {
            model.addAttribute("message", "Bạn đã hết số lần làm bài kiểm tra! Vui lòng quay lại vào ngày hôm sau.");
            return "front/exam_failed";
        }

        Result result = new Result();
        result.setExam(exam);
        result.setAccount(account);
        result.setEndTime(date);
        result.setStartTime(new Date(dateStart));
        result.setStatus(true);
        result.setResult("FAILED");
        resultService.save(result);

        if (isCompleteExamTime(dateStart, exam)) {
            result.setResult("FAILED");
            resultService.save(result);
            model.addAttribute("message", "Thời gian làm bài của bạn đã hết! Vui lòng làm lại sau!");
            return "front/exam_failed";
        }

        int index = 0;
        double score = 0;

        for(String value: requestParams.values()) {
            // break last list
            if (index == examRequest || index == timeRequest || index == courseIdRequest) {
                break;
            }
            Answer answer = saveAnswer(index, value, questionDTOList, result);
            questionDTOList.get(index).setChooseAnswer(answer.getChoose());
            answerList.add(answer);

            if (answer.isCorrect()) {
                score += 10.0;
            } else {
                score += 0.0;
            }
            index++;
        }

        score = FormatUtils.formatScore(score / answerList.size());
        if (score >= SCORE_PASSED) {
            result.setResult("PASSED");
        } else {
            result.setResult("FAILED");
        }

        result.setScore(score);
        resultService.save(result);

        model.addAttribute("examDTO", examMapper.toDTO(exam));
        model.addAttribute("questionDTOList", questionDTOList);
        model.addAttribute("resultDTO", resultMapper.toDTO(result));
        model.addAttribute("courseId", courseId);
        return "front/exam_score";
    }

    @PostMapping("/essay/submit")
    public String saveEssay(Model model, QuestionDTO questionDTO, Authentication authentication,
                       @RequestParam Map<String, String> requestParams,
                       @RequestParam long examId, @RequestParam long courseId, long dateStart) {
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Account account = customUserDetail.getAccount();
        Date date = new Date();

        Exam exam = examService.findById(examId);
        List<Result> resultList = resultService.findByAccountAndExamAndDate(account, exam, date);

        int numberOfDay = Integer.parseInt(exam.getNumberOfDay().substring(0, 1));
        if (isNumberOfDayLimit(numberOfDay, resultList.size())) {
            model.addAttribute("message", "Bạn đã hết số lần làm bài kiểm tra! Vui lòng quay lại vào ngày hôm sau.");
            return "front/exam_failed";
        }

        Result result = new Result();
        result.setExam(exam);
        result.setAccount(account);
        result.setEndTime(date);
        result.setStartTime(new Date(dateStart));
        result.setStatus(true);
        resultService.save(result);

        if (isCompleteExamTime(dateStart, exam)) {
            result.setResult("FAILED");
            resultService.save(result);
            model.addAttribute("message", "Thời gian làm bài của bạn đã hết! Vui lòng làm lại sau!");
            return "front/exam_failed";
        }

        Answer answer = new Answer();
        Question question = questionService.findById(questionDTO.getId());
        answer.setQuestion(question);
        if (questionDTO.getTemplateMul() != null && !ObjectUtils.isEmpty(questionDTO.getTemplateMul().getOriginalFilename())) {
            FileDTO fileDTOBack = fileUploadService.uploadFile(questionDTO.getTemplateMul(), "RESULT");
            answer.setChoose(fileDTOBack.getFileName());
        }
        answer.setCorrect(false);
        answer.setResult(result);
        answer.setStatus(true);

        result.setResult("PROGRESS");
        result.setScore(0);

        resultService.save(result);
        answerService.save(answer);

        model.addAttribute("examDTO", examMapper.toDTO(exam));
        model.addAttribute("questionDTOList", null);
        model.addAttribute("resultDTO", resultMapper.toDTO(result));
        model.addAttribute("courseId", courseId);
        return "front/exam_score";
    }

    @GetMapping("/essay/download/{questionId}")
    public void downloadDocument(@PathVariable long questionId, HttpServletResponse httpServletResponse) throws IOException {
        Question question = questionService.findById(questionId);
        File file = new File(ConstantUtil.RESOURCE_FILE_EXAM_DOWNLOAD + question.getAnswer());
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

    private boolean isCompleteExamTime(long timeStart, Exam exam) {
        Date date = new Date();
        Date startDate = new Date(timeStart);
        long timeExam = Long.parseLong(exam.getTotalTime().substring(0, 2));
        Date timeExamDate = new Date(startDate.getTime() + timeExam);
        return !date.after(timeExamDate);
    }

    private boolean isNumberOfDayLimit(int numberOfDay, int resultTimes) {
        return resultTimes >= numberOfDay;
    }

    private Answer saveAnswer(int index, String option, List<QuestionDTO> questionDTOList, Result result) {
        Answer answer = new Answer();
        answer.setResult(result);
        Question question = questionMapper.toEntity(questionDTOList.get(index));
        answer.setQuestion(question);
        answer.setChoose(option);

        String correct = questionDTOList.get(index).getAnswer();
        answer.setCorrect(correct.equalsIgnoreCase(option));
        answer.setStatus(true);
        return answerService.save(answer);
    }

}
