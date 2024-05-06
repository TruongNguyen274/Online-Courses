package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {

    private long id;
    private long courseId;
    private String content;
    private String createdDate;
    private boolean status;
    private boolean isCommentCourse;

    private AccountDTO ownerDTO;
    private long ownerId;

    private LectureDTO lectureDTO;
    private long lectureId;

}
