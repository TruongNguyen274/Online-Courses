package com.online.learning.service;

import com.online.learning.model.entity.Chapter;
import com.online.learning.model.entity.Course;

import java.util.List;

public interface ChapterService {

    List<Chapter> findAll();

    Chapter findById(long id);

    Chapter save(Chapter chapter);

    List<Chapter> findByCourse(Course course);

    List<Chapter> findByOwnerId(long ownerId);

    List<Chapter> findByCourseOrderBySortOrderAsc(Course course);

    Chapter findByCourseId(long id);

}
