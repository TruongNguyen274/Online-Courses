package com.online.learning.service;

import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Category;
import com.online.learning.model.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    List<Course> findAll();

    List<Course> findAllByActive();

    List<Course> findByOwner(Account owner);

    Page<Course> findByCategoryAndStatusIsTrue(Category category, Pageable pageable);

    List<Course> findByRelatedAndStatusIsTrue(Course course, Category category, int limit);

    Page<Course> searchByTitleAndStatusIsTrue(String title, Pageable pageable);

    Course findByTitle(String title);

    Course findById(long id);

    Course save(Course course);

//    Course save(CourseDTO courseDTO);

    List<Course> getRandomCourses(int limit);

}
