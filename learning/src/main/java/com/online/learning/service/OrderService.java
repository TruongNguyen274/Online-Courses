package com.online.learning.service;

import com.online.learning.model.dto.CourseDTO;
import com.online.learning.model.dto.OrderDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Course;
import com.online.learning.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    List<Order> findAll();

    Order save(OrderDTO orderDTO);

    Order checkout(OrderDTO orderDTO, CourseDTO courseDTO);

    Boolean delete(long id);

    Order findById(long id);

    Page<Order> findByAccount(Account account, Pageable pageable);

    List<Order> findByAccount(Account account);

    List<Order> findByCourse(Course course);

    boolean findCourseInAccount(Course course, Account account);

    Order saveEntity(Order order);

    List<Order> findByStatusIsTrue();

}
