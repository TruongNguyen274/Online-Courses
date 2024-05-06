package com.online.learning.service;

import com.online.learning.model.dto.ResultDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Exam;
import com.online.learning.model.entity.Result;

import java.util.Date;
import java.util.List;

public interface ResultService {
    List<Result> findAll();

    List<ResultDTO> findByCourseId(long courseId);

    List<ResultDTO> findByChapterId(long chapterId);

    List<ResultDTO> findByLectureId(long lectureId);

    Result findById(long id);

    List<Result> findByAccountAndExam(Account account, Exam exam);

    List<Result> findByAccountAndExamAndDate(Account account, Exam exam, Date date);

    Result save(Result result);

}
