package com.online.learning.service;

import com.online.learning.model.entity.Chapter;
import com.online.learning.model.entity.Course;
import com.online.learning.model.entity.Exam;
import com.online.learning.model.entity.Lecture;

import java.util.List;

public interface ExamService {

    List<Exam> findAll();

    Exam save(Exam exam);

    Exam findById(long id);

//    List<Exam> findByCourse(Course course);

    List<Exam> findByCourseId(long id);

    List<Exam> findByChapter(Chapter chapter);

    List<Exam> findByLecture(Lecture lecture);

}
