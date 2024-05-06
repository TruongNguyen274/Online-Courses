package com.online.learning.repository;

import com.online.learning.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findByTitle(String title);

    @Query(value = "SELECT COUNT(*) FROM course c JOIN category cate ON c.category_id = cate.id \n" +
            "WHERE cate.title = :title", nativeQuery = true)
    int countCoursesByCategory(String title);

    List<Category> findByStatusIsTrue();

}
