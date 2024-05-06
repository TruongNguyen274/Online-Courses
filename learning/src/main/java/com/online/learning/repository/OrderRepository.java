package com.online.learning.repository;

import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Course;
import com.online.learning.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByAccount(Account account, Pageable pageable);

    List<Order> findByAccount(Account account);

    List<Order> findByCourse(Course course);

    List<Order> findByStatusIsTrue();

}
