package com.online.learning.repository;

import com.online.learning.model.entity.Exam;
import com.online.learning.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    List<Question> findByExam(Exam exam);

}
