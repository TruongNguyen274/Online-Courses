package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class LearnDTO {

    private long courseId;
    private CourseDTO courseDTO;
    private Map<ChapterDTO, List<LectureDTO>> mapChapterByListLecture;

}
