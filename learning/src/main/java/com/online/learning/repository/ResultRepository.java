package com.online.learning.repository;

import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Exam;
import com.online.learning.model.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result,Long> {

    List<Result> findByAccountAndExamOrderByStartTimeDesc(Account account, Exam exam);

    @Query(value = "SELECT * FROM result r\n" +
            "WHERE r.account_id = :accountId AND r.exam_id = :examId\n" +
            "AND r.start_time BETWEEN :startDate AND :endDate\n" +
            "ORDER BY r.created_on DESC", nativeQuery = true)
    List<Result> findByAccountAndExamAndDate(long accountId, long examId, String startDate, String endDate);

}
