package com.online.learning.model.mapper;

import com.online.learning.model.dto.QuestionDTO;
import com.online.learning.model.entity.Question;

import java.util.List;

public interface QuestionMapper {
    QuestionDTO toDTO(Question question);

    List<QuestionDTO> toListDTO(List<Question> questionList);

    Question toEntity(QuestionDTO questionDTO);
}
