package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CourseDTO extends BaseDTO{

    private long id;
    private String lastUpdate;
    private String title;
    private String description;
    private String intro;
    private String price;
    private String newPrice;
    private String discount;
    private String duration;
    private String lecture;
    private String enrolled;
    private String avatar;
    private String summary;
    private boolean status;

    private MultipartFile avatarMul;

    private AccountDTO ownerDTO;
    private long ownerId;

    private CategoryDTO categoryDTO;
    private long categoryId;

}
