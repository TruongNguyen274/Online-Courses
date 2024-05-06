package com.online.learning.handler;

import com.online.learning.model.entity.Account;
import com.online.learning.service.custom.CustomUserDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String redirectURL = "/login";
        try {
            CustomUserDetail customUserDetails = (CustomUserDetail) authentication.getPrincipal();
            Account account = customUserDetails.getAccount();

            if (account != null) {
                redirectURL = switch (account.getRole().toUpperCase()) {
                    case "ADMIN" -> "/back/dashboard";
                    case "TEACHER" -> "/back/courses";
                    case "STUDENT" -> "/home";
                    default -> "/login";
                };
            }

            response.sendRedirect(redirectURL);
        } catch (Exception ex) {
            response.sendRedirect(redirectURL);
        }
    }



}
