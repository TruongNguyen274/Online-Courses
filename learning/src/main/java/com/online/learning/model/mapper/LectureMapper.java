package com.online.learning.model.mapper;

import com.online.learning.model.dto.LectureDTO;
import com.online.learning.model.entity.Lecture;

import java.util.List;

public interface LectureMapper {

    LectureDTO toDTO(Lecture lecture);

    List<LectureDTO> toListDTO(List<Lecture> lectures);

    Lecture toEntity(LectureDTO lectureDTO);
    
}
