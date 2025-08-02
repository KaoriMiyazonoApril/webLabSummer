package com.example.sports;

import com.example.sports.po.Account;
import com.example.sports.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class TomatoMallApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testDatabaseConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("数据库连接成功！");
        }
    }

    @Test
    void FirstAdmin(){
        Account account = new Account();
        account.setUsername("Makise Kurisu");
        account.setPassword(passwordEncoder.encode("123456"));
        account.setRole("Admin");
        account.setTelephone(10000000000L);

        accountRepository.save(account);
    }
}
