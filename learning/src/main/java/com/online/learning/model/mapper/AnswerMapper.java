package com.online.learning.model.mapper;

import com.online.learning.model.dto.AnswerDTO;
import com.online.learning.model.entity.Answer;

import java.util.List;

public interface AnswerMapper {
    AnswerDTO toDTO(Answer answer);

    List<AnswerDTO> toListDTO(List<Answer> answerList);

    Answer toEntity(AnswerDTO answerDTO);
}
