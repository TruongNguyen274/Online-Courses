package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class QuestionDTO {

    private long id;
    private String title;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;
    private boolean status;
    private boolean isQuiz;

    private ExamDTO examDTO;
    private long examId;
    private String examTitle;
    private String examType;

    private String chooseAnswer;

    private MultipartFile templateMul;
    private String pathFileExcel;

}
