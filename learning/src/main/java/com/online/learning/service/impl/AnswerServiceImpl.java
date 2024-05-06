package com.online.learning.service.impl;

import com.online.learning.model.entity.Answer;
import com.online.learning.model.entity.Result;
import com.online.learning.repository.AnswerRepository;
import com.online.learning.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    @Override
    public Answer findById(long id) {
        return answerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Answer> findByResult(Result result) {
        return answerRepository.findByResult(result);
    }

    @Override
    public List<Answer> save(List<Answer> answers) {
        return answerRepository.saveAll(answers);
    }

    @Override
    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }

}
