package com.online.learning.service.impl;

import com.online.learning.model.entity.Chapter;
import com.online.learning.model.entity.Course;
import com.online.learning.repository.ChapterRepository;
import com.online.learning.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Override
    public List<Chapter> findAll() {
        return chapterRepository.findAll();
    }

    @Override
    public Chapter findById(long id) {
        return chapterRepository.findById(id).orElse(null);
    }

    @Override
    public Chapter save(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    @Override
    public List<Chapter> findByCourse(Course course) {
        return chapterRepository.findByCourse(course);
    }

    @Override
    public List<Chapter> findByOwnerId(long ownerId) {
        return chapterRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Chapter> findByCourseOrderBySortOrderAsc(Course course) {
        return chapterRepository.findByCourseOrderBySortOrderAsc(course);
    }

    @Override
    public Chapter findByCourseId(long id) {
        return chapterRepository.findByCourseId(id);
    }
}
