package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class OrderDTO extends BaseDTO{

    private long id;
    private int price;
    private int totalCost;
    private String timeOrder;
    private boolean status;
    private String priceView;

    private AccountDTO accountDTO;
    private long accountId;

    private CourseDTO courseDTO;
    private long courseId;

    private String invoice;
    private MultipartFile invoiceMul;

}
