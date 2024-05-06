package com.online.learning.service.impl;

import com.online.learning.model.entity.Chapter;
import com.online.learning.model.entity.Lecture;
import com.online.learning.repository.LectureRepository;
import com.online.learning.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {

    @Autowired
    private LectureRepository lectureRepository;

    @Override
    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    @Override
    public Lecture findById(long id) {
        return lectureRepository.findById(id).orElse(null);
    }

    @Override
    public Lecture save(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    @Override
    public List<Lecture> findByChapter(Chapter chapter) {
        return lectureRepository.findByChapter(chapter);
    }

    @Override
    public List<Lecture> findByOwnerId(long ownerId) {
        return lectureRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Lecture> findByChapterOrderBySortOrderAsc(Chapter chapter) {
        return lectureRepository.findByChapterOrderBySortOrderAsc(chapter);
    }

}
