package com.online.learning.model.mapper.impl;

import com.online.learning.model.dto.CategoryDTO;
import com.online.learning.model.entity.Category;
import com.online.learning.model.mapper.CategoryMapper;
import com.online.learning.service.CategoryService;
import com.online.learning.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Autowired
    private CategoryService categoryService;

    @Override
    public CategoryDTO toDTO(Category category) {
        if (category == null){
            return null;
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setTitle(category.getTitle());
        categoryDTO.setStatus(category.isStatus());

        categoryDTO.setAmountCourse(categoryService.countCoursesByCategory(category.getTitle()));
        categoryDTO.setSlug(FormatUtils.toSlug(category.getTitle()));

        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> toListDTO(List<Category> categoryList) {
        if (categoryList == null) {
            return null;
        }
        List<CategoryDTO> list = new ArrayList<>(categoryList.size());
        for (Category category : categoryList) {
            CategoryDTO categoryDTO = toDTO(category);
            if (categoryDTO != null) {
                list.add(categoryDTO);
            }
        }
        return list;
    }

    @Override
    public Category toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        Category category = new Category();
        category.setTitle(categoryDTO.getTitle());
        category.setDescription(categoryDTO.getDescription());
        category.setStatus(categoryDTO.isStatus());
        return category;
    }
}
