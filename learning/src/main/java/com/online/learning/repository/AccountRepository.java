package com.online.learning.repository;

import com.online.learning.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByUsernameAndStatusIsTrue(String username);

    Account findByPhone(String phone);

    Account findByEmail(String email);

    List<Account> findByRole( String role);

    List<Account> findByStatusIsTrue();
}
