package com.online.learning.validator;

import com.online.learning.service.AccountService;
import com.online.learning.model.dto.AccountDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {

    @Autowired
    private AccountService accountService;


    @Override
    public boolean supports(Class<?> clazz) {
        return AccountDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            AccountDTO accountDTO = (AccountDTO) target;
            Account account = null;
            // verify title
            if (ValidatorUtil.isEmpty(accountDTO.getFullName().trim())) {
                errors.rejectValue("fullName", "Vui lòng nhập họ tên!",
                        "Vui lòng nhập họ tên!");
            } else {
                if (accountDTO.getFullName().length() > 50) {
                    errors.rejectValue("fullName", "Tên không được vượt quá 255 kí tự", "Tên không được vượt quá 255 kí tự");
                } else if (ValidatorUtil.isName(accountDTO.getFullName())) {
                    errors.rejectValue("fullName", "Họ và Tên không được chứa số!",
                            "Họ và Tên không được chứa số!");
                }
            }

            // verify email
            if (ValidatorUtil.isEmpty(accountDTO.getEmail().trim())) {
                errors.rejectValue("email", "Vui lòng nhập Địa chỉ Email!",
                        "Vui lòng nhập Địa chỉ Email!");
            } else {
                if (!ValidatorUtil.checkEmail(accountDTO.getEmail())) {
                    errors.rejectValue("email", "Email không đúng định dạng!",
                            "Email không đúng định dạng!");
                } else if (accountDTO.getEmail().length() > 150) {
                    errors.rejectValue("email", "Email không được vượt quá 150 kí tự", "Tên không được vượt quá 150 kí tự");
                } else {
                    account = accountService.findByEmail(accountDTO.getEmail().trim());
                    if (account != null && account.getId() != accountDTO.getId()) {
                        errors.rejectValue("email", "Địa chỉ Email đã được đăng ký!",
                                "Địa chỉ Email đã được đăng ký!");
                    }
                }
            }

            // verify tenDangNhap
            if (ValidatorUtil.isEmpty(accountDTO.getUsername().trim())) {
                errors.rejectValue("username", "Vui lòng nhập Tên Đăng Nhập!",
                        "Vui lòng nhập Tên Đăng Nhập!");
            }

            // verify soDienThoai
            String phone = accountDTO.getPhone().trim();
            if (ValidatorUtil.isEmpty(phone)) {
                errors.rejectValue("phone", "Vui lòng nhập Số Điện Thoại!",
                        "Vui lòng nhập Số Điện Thoại!");
            } else {
                if (!ValidatorUtil.checkPhone(phone)) {
                    errors.rejectValue("phone", "Vui lòng nhập đúng định dạng", "Vui lòng nhập đúng định dạng");
                } else {
                    account = accountService.findByPhone(phone);
                    if (account != null && account.getId() != accountDTO.getId()) {
                        errors.rejectValue("phone", "Số Điện Thoại đã được đăng ký!",
                                "Số Điện Thoại đã được đăng ký!");
                    }
                }
            }

        } catch (Exception e) {
            errors.rejectValue("msg", "Có lỗi xảy ra, vui lòng thử lại!",
                    "Có lỗi xảy ra, vui lòng thử lại!");
        }
    }
}
