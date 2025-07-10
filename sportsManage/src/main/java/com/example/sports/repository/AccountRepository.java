package com.example.sports.repository;

import com.example.sports.po.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByTelephone(String phone);
    Account findByUsername(String username);
    Account findByUsernameAndPassword(String username,String password);
}
