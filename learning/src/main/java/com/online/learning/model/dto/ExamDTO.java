package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ExamDTO {

    private long id;
    private String title;
    private String description;
    private String type;
    private String kindExam;
    private String totalTime;
    private String numberOfDay;

    private CourseDTO courseDTO;
    private long courseId;

    private ChapterDTO chapterDTO;
    private long chapterId;

    private LectureDTO lectureDTO;
    private long lectureId;

    private boolean status;

    private MultipartFile templateMul;
    private String pathFileExcel;

    private String questionFailed;
    private List<QuestionDTO> questionDTOList;

    private boolean examPassed;
    private String result;

}
