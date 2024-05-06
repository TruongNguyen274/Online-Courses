package com.online.learning.service;

import com.online.learning.model.dto.CategoryDTO;
import com.online.learning.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    List<Category> findAllByActive();

    Category findByTitle(String title);

    int countCoursesByCategory(String title);

    Category save(CategoryDTO categoryDTO);

    Boolean delete(long id);

    Category findById(long id);
}
