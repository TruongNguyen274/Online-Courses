package com.online.learning.service.impl;

import com.online.learning.repository.CategoryRepository;
import com.online.learning.service.CategoryService;
import com.online.learning.model.dto.CategoryDTO;
import com.online.learning.model.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findAllByActive() {
        return categoryRepository.findByStatusIsTrue();
    }

    @Override
    public Category findByTitle(String title) {
        return categoryRepository.findByTitle(title);
    }

    @Override
    public int countCoursesByCategory(String title) {
        return categoryRepository.countCoursesByCategory(title);
    }

    @Override
    public Category save(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryDTO.getId()).orElse(null);
        if (category == null) {
            category = new Category();
            category.setStatus(true);

        }
        if (!StringUtils.isEmpty(categoryDTO.getTitle())) {
            category.setTitle(categoryDTO.getTitle());
        }
        if (!StringUtils.isEmpty(categoryDTO.getDescription())) {
            category.setDescription(categoryDTO.getDescription());
        }
        if (!StringUtils.isEmpty(categoryDTO.isStatus())) {
            category.setStatus(categoryDTO.isStatus());
        }

        return categoryRepository.save(category);
    }

    @Override
    public Boolean delete(long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null){
            return false;
        }
        category.setStatus(false);
        categoryRepository.save(category);
        return true;
    }

    @Override
    public Category findById(long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
