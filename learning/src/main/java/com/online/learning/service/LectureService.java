package com.online.learning.service;

import com.online.learning.model.entity.Chapter;
import com.online.learning.model.entity.Lecture;

import java.util.List;

public interface LectureService {

    List<Lecture> findAll();

    Lecture findById(long id);

    Lecture save(Lecture lecture);

    List<Lecture> findByChapter(Chapter chapter);

    List<Lecture> findByOwnerId(long ownerId);

    List<Lecture> findByChapterOrderBySortOrderAsc(Chapter chapter);

}
