package com.online.learning.model.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BaseDTO {

    private long id;
    private long version;
    private String createdOn;
    private String updatedOn;



}
