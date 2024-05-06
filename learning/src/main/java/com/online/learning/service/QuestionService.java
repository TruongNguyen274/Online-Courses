package com.online.learning.service;

import com.online.learning.model.entity.Exam;
import com.online.learning.model.entity.Question;

import java.util.List;

public interface QuestionService {

    List<Question> findAll();

    List<Question> findByExam(Exam exam);

    Question save(Question question);

    Question findById(long id);
}
