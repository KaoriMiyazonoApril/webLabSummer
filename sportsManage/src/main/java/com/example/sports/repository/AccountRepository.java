package com.example.sports.repository;

import com.example.sports.po.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    public Account findByTelephone(Long telephone);
}
