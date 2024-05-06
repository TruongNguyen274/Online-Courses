package com.online.learning.service.impl;

import com.online.learning.model.entity.Chapter;
import com.online.learning.model.entity.Course;
import com.online.learning.model.entity.Exam;
import com.online.learning.model.entity.Lecture;
import com.online.learning.repository.ExamRepository;
import com.online.learning.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Override
    public List<Exam> findAll() {
        return examRepository.findAll();
    }

    @Override
    public Exam findById(long id) {
        return examRepository.findById(id).orElse(null);
    }

    @Override
    public List<Exam> findByCourseId(long id) {
        return examRepository.findByCourseId(id);
    }

    @Override
    public List<Exam> findByChapter(Chapter chapter) {
        return examRepository.findByChapter(chapter);
    }

    @Override
    public List<Exam> findByLecture(Lecture lecture) {
        return examRepository.findByLecture(lecture);
    }

//    @Override
//    public List<Exam> findByCourse(Course course) {
//        return examRepository.findByCourse(course);
//    }

    @Override
    public Exam save(Exam exam) {
        return examRepository.save(exam);
    }

}
