package com.online.learning.service.impl;

import com.online.learning.model.dto.AccountDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.repository.AccountRepository;
import com.online.learning.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account save(AccountDTO accountDTO) {
        Account account = accountRepository.findById(accountDTO.getId()).orElse(null);

        if (account == null) {
            account = new Account();
            account.setStatus(true);
        }
        account.setUsername(accountDTO.getUsername());
        account.setFullName(accountDTO.getFullName());
        account.setEmail(accountDTO.getEmail());
        account.setPhone(accountDTO.getPhone());
        account.setPassword(accountDTO.getPassword());
        account.setRole(accountDTO.getRole());
        account.setStatus(accountDTO.isStatus());

        return accountRepository.save(account);
    }

    @Override
    public Boolean delete(long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return false;
        }
        account.setStatus(false);
        accountRepository.save(account);
        return true;
    }

    @Override
    public Account findById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsernameAndStatusIsTrue(username).orElse(null);
    }

    @Override
    public Account findByPhone(String phone) {
        return accountRepository.findByPhone(phone);
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Account register(AccountDTO accountDTO) {
        if (accountDTO == null) {
            return null;
        }

        Account account = new Account();

        // role
        if (accountDTO.getType() == null || accountDTO.getType().equalsIgnoreCase("STUDENT")) {
            account.setRole("STUDENT");
        } else {
            account.setRole("TEACHER");
        }

        // account
        account.setId(accountDTO.getId());
        account.setFullName(accountDTO.getFullName().trim());
        account.setUsername(accountDTO.getEmail().trim());
        String encodedPassword = passwordEncoder.encode(accountDTO.getPassword());
        account.setPassword(encodedPassword);
        account.setPhone(accountDTO.getPhone());
        account.setEmail(accountDTO.getEmail().trim());
        account.setStatus(true);

        return accountRepository.save(account);

    }

    @Override
    public List<Account> findByRole(String role) {
        return accountRepository.findByRole(role);
    }

    @Override
    public List<Account> findByStatusIsTrue() {
        return accountRepository.findByStatusIsTrue();
    }

}
