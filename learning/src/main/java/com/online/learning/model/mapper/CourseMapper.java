package com.online.learning.model.mapper;

import com.online.learning.model.dto.CourseDTO;
import com.online.learning.model.entity.Course;

import java.util.List;

public interface CourseMapper {

    CourseDTO toDTO(Course course);

    List<CourseDTO> toListDTO(List<Course> courses);

    Course toEntity(CourseDTO courseDTO);

}
