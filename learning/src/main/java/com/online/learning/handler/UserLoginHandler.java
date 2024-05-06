package com.online.learning.handler;

import com.online.learning.model.dto.AccountDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.service.custom.CustomUserDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class UserLoginHandler {

    @ModelAttribute("userLogin")
    public AccountDTO getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return null;

        }
        CustomUserDetail customUserDetails = (CustomUserDetail) authentication.getPrincipal();
        Account account = null;
        if (customUserDetails != null) {
            account = customUserDetails.getAccount();
        }
        AccountDTO accountDTO = null;
        if (account != null) {
            accountDTO = new AccountDTO();
            accountDTO.setFullName(account.getFullName());
            accountDTO.setUsername(account.getUsername());
            accountDTO.setRole(account.getRole());
        }
        return accountDTO;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

}
