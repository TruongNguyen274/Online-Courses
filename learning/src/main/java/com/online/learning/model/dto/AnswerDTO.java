package com.online.learning.model.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDTO {

    private long id;
    private String option;
    private boolean correct;
    private boolean status;

    private ResultDTO resultDTO;
    private long resultId;

    private QuestionDTO questionDTO;
    private long questionId;

}
