package com.online.learning.service.impl;

import com.online.learning.model.dto.ResultDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Exam;
import com.online.learning.model.entity.Result;
import com.online.learning.repository.ResultRepository;
import com.online.learning.repository.custom.ResultCustomRepository;
import com.online.learning.service.ResultService;
import com.online.learning.utils.ConstantUtil;
import com.online.learning.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ResultCustomRepository resultCustomRepository;

    @Override
    public List<Result> findAll() {
        return resultRepository.findAll();
    }

    @Override
    public Result findById(long id) {
        return resultRepository.findById(id).orElse(null);
    }

    @Override
    public Result save(Result result) {
        return resultRepository.save(result);
    }

    @Override
    public List<Result> findByAccountAndExam(Account account, Exam exam) {
        return resultRepository.findByAccountAndExamOrderByStartTimeDesc(account, exam);
    }

    @Override
    public List<Result> findByAccountAndExamAndDate(Account account, Exam exam, Date date) {
        String startDate = DateUtil.convertDateToString(date, ConstantUtil.DATE_PATTERN) + " 00:00:00";
        String endDate = DateUtil.convertDateToString(date, ConstantUtil.DATE_PATTERN) + " 23:59:59";
        return resultRepository.findByAccountAndExamAndDate(account.getId(), exam.getId(), startDate, endDate);
    }

    @Override
    public List<ResultDTO> findByCourseId(long courseId) {
        return resultCustomRepository.findByCourseId(courseId);
    }

    @Override
    public List<ResultDTO> findByChapterId(long chapterId) {
        return resultCustomRepository.findByChapterId(chapterId);
    }

    @Override
    public List<ResultDTO> findByLectureId(long lectureId) {
        return resultCustomRepository.findByLectureId(lectureId);
    }

}
