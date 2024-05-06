package com.online.learning.model.mapper;

import com.online.learning.model.dto.ExamDTO;
import com.online.learning.model.entity.Exam;

import java.util.List;

public interface ExamMapper {
    ExamDTO toDTO(Exam exam);

    List<ExamDTO> toListDTO(List<Exam> examList);

    Exam toEntity(ExamDTO examDTO);
}
