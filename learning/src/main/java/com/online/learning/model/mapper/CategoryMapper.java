package com.online.learning.model.mapper;

import com.online.learning.model.dto.CategoryDTO;
import com.online.learning.model.entity.Category;

import java.util.List;

public interface CategoryMapper {
    CategoryDTO toDTO(Category category);

    List<CategoryDTO> toListDTO(List<Category> categoryList);

    Category toEntity(CategoryDTO categoryDTO);
}
