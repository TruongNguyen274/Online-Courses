package com.online.learning.repository;

import com.online.learning.model.entity.Chapter;
import com.online.learning.model.entity.Course;
import com.online.learning.model.entity.Exam;
import com.online.learning.model.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Long> {

    List<Exam> findByCourseId(long id);

    List<Exam> findByChapter(Chapter chapter);

    List<Exam> findByLecture(Lecture lecture);

}
