package com.online.learning.validator;

import com.online.learning.model.dto.AccountDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.service.AccountService;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterValidator implements Validator {

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

            // verify fullName
            if (ValidatorUtil.isEmpty(accountDTO.getFullName().trim())) {
                errors.rejectValue("fullName", "Vui lòng nhập Họ và Tên!",
                        "Vui lòng nhập Họ và Tên!");
            } else if (ValidatorUtil.isName(accountDTO.getFullName())) {
                errors.rejectValue("fullName", "Họ và Tên không được chứa số!",
                        "Họ và Tên không được chứa số!");
            } else if (accountDTO.getFullName().length() > 50){
                errors.rejectValue("fullName","Tên không được vượt quá 50 kí tự","Tên không được vượt quá 50 kí tự");
            }

            //verify email
            if (ValidatorUtil.isEmpty(accountDTO.getEmail().trim())) {
                errors.rejectValue("email", "Vui lòng nhập Email", "Vui lòng nhập Email");
            } else {
                if (!ValidatorUtil.checkEmail(accountDTO.getEmail())) {
                    errors.rejectValue("email", "Vui lòng nhập Email đúng định dạng", "Vui lòng nhập Email đúng định dạng");
                } else if (accountDTO.getEmail().length() > 150){
                    errors.rejectValue("email","Email không được vượt quá 150 kí tự","Tên không được vượt quá 150 kí tự");
                } else {
                    Account account = accountService.findByEmail(accountDTO.getEmail());
                    if (account != null && account.getEmail().equals(accountDTO.getEmail())) {
                        errors.rejectValue("email", "Email đã được đăng ký!",
                                "Email đã được đăng ký!");
                    }
                }
            }

            // verify password
            if (ValidatorUtil.isEmpty(accountDTO.getPassword().trim())) {
                errors.rejectValue("password", "Vui lòng nhập Mật Khẩu!",
                        "Vui lòng nhập Mật Khẩu!");
            } else {
                if (accountDTO.getPassword().length() < 6) {
                    errors.rejectValue("password", "Vui lòng nhập Mật Khẩu lớn hơn 6 ký tự!",
                            "Vui lòng nhập Mật Khẩu lớn hơn 6 ký tự!");
                }
            }

            // verify confirmPassword
            if (ValidatorUtil.isEmpty(accountDTO.getConfirmPassword().trim())) {
                errors.rejectValue("confirmPassword", "Vui lòng nhập Xác Nhận Mật Khẩu!",
                        "Vui lòng nhập Xác Nhận Mật Khẩu!");
            } else {
                if (accountDTO.getConfirmPassword().length() < 6) {
                    errors.rejectValue("confirmPassword", "Vui lòng nhập Xác Nhận Mật Khẩu lớn hơn 6 ký tự!",
                            "Vui lòng nhập Xác Nhận Mật Khẩu lớn hơn 6 ký tự!");
                } else if (!accountDTO.getConfirmPassword().equals(accountDTO.getPassword())) {
                    errors.rejectValue("confirmPassword", "Xác Nhận Mật Khẩu không trùng khớp với Mật Khẩu!",
                            "Xác Nhận Mật Khẩu không trùng khớp với Mật Khẩu!");
                }
            }

            // verify phone
            if (ValidatorUtil.isEmpty(accountDTO.getPhone().trim())) {
                errors.rejectValue("phone", "Vui lòng nhập Số Điện Thoại!",
                        "Vui lòng nhập Số Điện Thoại!");
            } else {
                if (!ValidatorUtil.checkPhone(accountDTO.getPhone())) {
                    errors.rejectValue("phone", "Vui lòng nhập đúng định dạng", "Vui lòng nhập đúng định dạng");
                }
                else {
                    Account account = accountService.findByPhone(accountDTO.getPhone());
                    if (account != null && account.getPhone().equals(accountDTO.getPhone())) {
                        errors.rejectValue("phone", "Số Điện Thoại đã được đăng ký!",
                                "Số Điện Thoại đã được đăng ký!");
                    }
                }
            }

            // verify type
            if (ValidatorUtil.isEmpty(accountDTO.getType())) {
                errors.rejectValue("type", "Vui lòng chọn Loại Tài Khoản!",
                        "Vui lòng chọn Loại Tài Khoản!");
            }
        } catch(Exception e) {
            errors.rejectValue("msg", "Có lỗi xảy ra, vui lòng thử lại!",
                    "Có lỗi xảy ra, vui lòng thử lại!");
        }

    }
}
