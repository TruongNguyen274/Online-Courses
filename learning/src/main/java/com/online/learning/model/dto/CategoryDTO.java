package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    private long id;
    private String title;
    private String description;
    private String slug;
    private int amountCourse;
    private boolean status;

    public CategoryDTO() {
    }

    public CategoryDTO(long id, String title, String description, int amountCourse, boolean status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amountCourse = amountCourse;
        this.status = status;
    }
}
