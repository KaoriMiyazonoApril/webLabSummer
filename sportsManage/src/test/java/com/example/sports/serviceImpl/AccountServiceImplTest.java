package com.example.sports.serviceImpl;

import com.example.sports.exception.SportsException;
import com.example.sports.service.AccountService;
import com.example.sports.vo.AccountVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;

    @Test
    void testGetUserByUsername_Success() {
        // 先确保存在该测试用户
        AccountVO result = accountService.getUserByUsername("123");
        assertNotNull(result);
        assertEquals("123", result.getUsername());
    }

    @Test
    void testGetUserByUsername_UserNotFound() {
        assertThrows(SportsException.class, () -> {
            accountService.getUserByUsername("nonExistingUser");
        });
    }
}