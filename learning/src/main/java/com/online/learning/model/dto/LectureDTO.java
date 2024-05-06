package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class LectureDTO {

    private long id;
    private String title;
    private String linkVideo;
    private String linkResource;
    private String timeLesson;
    private String sortOrder;
    private String content;
    private boolean status;

    private MultipartFile videoMul;
    private MultipartFile resourceMul;

    private ChapterDTO chapterDTO;
    private long chapterId;

    private long ownerId;
    private boolean examPassed;

}
