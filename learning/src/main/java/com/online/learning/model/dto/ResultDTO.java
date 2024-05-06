package com.online.learning.model.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ResultDTO {

    private long id;
    private String startTime;
    private String endTime;
    private double score;
    private String result; // Passed, Failed

    private AccountDTO accountDTO;
    private long accountId;

    private ExamDTO examDTO;
    private long examId;

    private List<AnswerDTO> answerDTOList;

    private boolean status;

}
