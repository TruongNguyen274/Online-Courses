package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChapterDTO implements Comparable<ChapterDTO> {

    private long id;
    private String title;
    private String description;
    private String totalLesson;
    private String sortOrder;
    private boolean status;

    private CourseDTO courseDTO;
    private long courseId;

    private List<LectureDTO> lectureList;
    private List<ExamDTO> examDTOList;

    private long ownerId;
    private boolean examPassed;

    @Override
    public int compareTo(ChapterDTO chapterDTO) {
        return Integer.parseInt(this.sortOrder) - Integer.parseInt(chapterDTO.sortOrder);
    }

}
