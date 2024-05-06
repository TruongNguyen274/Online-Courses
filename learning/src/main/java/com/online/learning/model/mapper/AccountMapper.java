package com.online.learning.model.mapper;

import com.online.learning.model.dto.AccountDTO;
import com.online.learning.model.entity.Account;

import java.util.List;

public interface AccountMapper {
    AccountDTO toDTO(Account account);

    List<AccountDTO> toListDTO(List<Account> accountList);

    Account toEntity(AccountDTO accountDTO);
}
