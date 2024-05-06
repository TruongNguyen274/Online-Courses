package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryDTO {

    private long id;
    private long courseId;
    private long chapterId;
    private int progress;

    private AccountDTO accountDTO;
    private long accountId;

    private LectureDTO lectureDTO;
    private long lectureId;

}
