package com.online.learning.service;

import com.online.learning.model.dto.AccountDTO;
import com.online.learning.model.dto.AccountDTO;
import com.online.learning.model.entity.Account;

import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Account save(AccountDTO accountDTO);

    Boolean delete(long id);

    Account findById(long id);

    Account findByUsername(String username);

    Account findByPhone(String phone);

    Account findByEmail(String email);

    Account register(AccountDTO accountDTO);

    List<Account> findByRole( String role);

    List<Account> findByStatusIsTrue();
}
