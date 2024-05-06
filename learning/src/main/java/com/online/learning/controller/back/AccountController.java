package com.online.learning.controller.back;

import com.online.learning.service.AccountService;
import com.online.learning.model.dto.AccountDTO;
import com.online.learning.model.dto.MessageDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.mapper.impl.AccountMapperImpl;
import com.online.learning.utils.ValidatorUtil;
import com.online.learning.validator.AccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/back/account")
public class AccountController {

    private String redirectUrl = "/back/account";

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapperImpl accountMapper;

    @Autowired
    private AccountValidator accountValidator;

    @Autowired
    private ValidatorUtil validatorUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = {"", "/"})
    public String findAll(Model model) {
        try {
            List<Account> accountList = accountService.findAll();
            model.addAttribute("accountList", accountMapper.toListDTO(accountList));

            return "back/account_list";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:" + redirectUrl;
        }
    }

    @GetMapping(value = {"/form"})
    public String create(Model model) {
        try {
            List<Account> accountList = accountService.findAll();
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setStatus(true);
            model.addAttribute("accountDTO", accountDTO);
            model.addAttribute("accountList", accountMapper.toListDTO(accountList));
            return "back/account_form";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:" + redirectUrl;
        }
    }

    @GetMapping(value = {"/form/{id}"})
    public String edit(Model model, @PathVariable long id, @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            Account account = accountService.findById(id);
            if (account == null) {
                return "redirect:" + redirectUrl;
            }

            model.addAttribute("accountDTO", accountMapper.toDTO(account));
            model.addAttribute("errorList", new HashMap<>());

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else { 
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/account_form";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:" + redirectUrl;
        }
    }

    @PostMapping(value = "/form")
    public String save(Model model, AccountDTO accountDTO, BindingResult bindingResult) {
        try {
            // verify value
            accountValidator.validate(accountDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                model.addAttribute("errorList", validatorUtil.toErrors(bindingResult.getFieldErrors()));
                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                return "back/account_form";
            } else {
                Account checkAccount = accountService.findById(accountDTO.getId());
                if (checkAccount == null) {
                    String password = "123456";
                    String encodedPassword = passwordEncoder.encode(password);
                    accountDTO.setPassword(encodedPassword);
                } else {
                    accountDTO.setPassword(checkAccount.getPassword());
                }

                // save
                Account account = accountService.save(accountDTO);

                redirectUrl = "/back/account/form/" + account.getId() + "?action=save&status=success";
                return "redirect:" + redirectUrl;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:" + redirectUrl;
        }
    }

    @GetMapping(value = {"/delete/{id}"})
    public String delete(Model model, @PathVariable long id) {
        try {
            accountService.delete(id);
            redirectUrl = "/back/account" + "?action=save&status=success";

            return "redirect:" + redirectUrl;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:" + redirectUrl;
        }
    }

}
