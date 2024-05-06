package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {

    private long id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String role;
    private String type;
    private boolean status;

    private String oldPassword;
    private String confirmPassword;
    private String newPassword;

}
