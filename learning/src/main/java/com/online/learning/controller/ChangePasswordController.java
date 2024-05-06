package com.online.learning.controller;


import com.online.learning.model.dto.AccountDTO;
import com.online.learning.model.dto.MessageDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.mapper.AccountMapper;
import com.online.learning.service.AccountService;
import com.online.learning.service.custom.CustomUserDetail;
import com.online.learning.validator.PasswordValidator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/change-password")

public class ChangePasswordController {

    private static final String REDIRECT_URL = "/change-password";

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = {"","/"})
    public String view(Model model, Authentication authentication) {
        try {
            CustomUserDetail customUserDetails = (CustomUserDetail) authentication.getPrincipal();
            Account customAccount = customUserDetails.getAccount();
            Account account = accountService.findById(customAccount.getId());

            model.addAttribute("accountDTO", accountMapper.toDTO(account));

            return "change_password";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping(value = {"", "/"})
    public String save(Model model, Authentication authentication, AccountDTO accountDTO, BindingResult bindingResult) {
        try {
            CustomUserDetail customUserDetails = (CustomUserDetail) authentication.getPrincipal();
            Account customAccount = customUserDetails.getAccount();
            accountDTO.setId(customAccount.getId());

            Account account = accountService.findById(customAccount.getId());

            // verify value
            passwordValidator.validate(accountDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                model.addAttribute("accountDTO", accountMapper.toDTO(account));
                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                return "change_password";
            } else {
                // save
                String encodedPassword = passwordEncoder.encode(accountDTO.getNewPassword());
                account.setPassword(encodedPassword);
                accountService.save(accountMapper.toDTO(account));

                String redirectUrl = "/logout";
                return "redirect:" + redirectUrl;
            }
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }


}
