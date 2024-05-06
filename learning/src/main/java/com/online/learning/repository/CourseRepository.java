package com.online.learning.repository;

import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Category;
import com.online.learning.model.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByOwner(Account owner);

    Page<Course> findByStatusTrue(Pageable pageable);

    Page<Course> findByCategoryAndStatusTrue(Category category, Pageable pageable);

    @Query(value = """
            SELECT * FROM course c\s
            WHERE c.id != :courseId AND c.category_id = :categoryId AND c.status = TRUE\s
            ORDER BY RAND() LIMIT :limit""", nativeQuery = true)
    List<Course> findByRelated(long courseId, long categoryId, int limit);

    Course findByTitleAndStatusTrue(String title);

    @Query(value = """
            SELECT * FROM course c\s
            WHERE c.title LIKE %?1% AND c.status = TRUE""", nativeQuery = true)
    Page<Course> searchByTitle(String title, Pageable pageable);

    List<Course> findByStatusIsTrue();



}
