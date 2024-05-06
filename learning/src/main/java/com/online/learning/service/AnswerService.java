package com.online.learning.service;

import com.online.learning.model.entity.Answer;
import com.online.learning.model.entity.Result;

import java.util.List;

public interface AnswerService {
    List<Answer> findAll();

    Answer save(Answer answer);

    Answer findById(long id);

    List<Answer> findByResult(Result result);

    List<Answer> save(List<Answer> answers);
}
