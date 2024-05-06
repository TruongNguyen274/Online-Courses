package com.online.learning.service.impl;

import com.online.learning.model.entity.Exam;
import com.online.learning.model.entity.Question;
import com.online.learning.repository.QuestionRepository;
import com.online.learning.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    @Override
    public List<Question> findByExam(Exam exam) {
        return questionRepository.findByExam(exam);
    }

    @Override
    public Question findById(long id) {
        return questionRepository.findById(id).orElse(null);
    }

    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
    }

}
