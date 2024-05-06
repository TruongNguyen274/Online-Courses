package com.online.learning.model.mapper.impl;

import com.online.learning.model.dto.AccountDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.mapper.AccountMapper;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountMapperImpl implements AccountMapper {
    @Override
    public AccountDTO toDTO(Account account) {
        if (account == null){
            return null;
        }
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setEmail(account.getEmail());
        accountDTO.setPhone(account.getPhone());
        accountDTO.setFullName(account.getFullName());
        accountDTO.setUsername(account.getUsername());
        accountDTO.setRole(account.getRole());
        accountDTO.setStatus(account.isStatus());
        return accountDTO;
    }

    @Override
    public List<AccountDTO> toListDTO(List<Account> accountList) {
        if (accountList == null) {
            return null;
        }
        List<AccountDTO> list = new ArrayList<>(accountList.size());
        for (Account account : accountList) {
            AccountDTO accountDTO = toDTO(account);
            if (accountDTO != null) {
                list.add(accountDTO);
            }
        }
        return list;
    }

    @Override
    public Account toEntity(AccountDTO accountDTO) {
        if (accountDTO == null) {
            return null;
        }
        Account account = new Account();
        if (!ValidatorUtil.isEmpty(accountDTO.getEmail())) {
            account.setEmail(accountDTO.getEmail().trim());
        }
        if (!ValidatorUtil.isEmpty(accountDTO.getPhone())) {
            account.setPhone(accountDTO.getPhone().trim());
        }
        account.setRole(accountDTO.getRole());
        if (accountDTO.getUsername() != null) {
            account.setUsername(accountDTO.getUsername().trim());
        }

        account.setFullName(accountDTO.getFullName());
        account.setPassword(accountDTO.getPassword());
        account.setStatus(accountDTO.isStatus());
        return account;
    }
}
