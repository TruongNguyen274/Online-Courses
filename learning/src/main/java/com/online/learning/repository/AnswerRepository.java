package com.online.learning.repository;

import com.online.learning.model.entity.Answer;
import com.online.learning.model.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {

    List<Answer> findByResult(Result result);

}
