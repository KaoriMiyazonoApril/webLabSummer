package com.example.sports.util;

import com.example.sports.po.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: DingXiaoyu
 * @Date: 0:28 2023/11/26
 * 你可以通过这个类的方法来获得当前用户的信息。
 */
@Component
public class SecurityUtil {
    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    HttpServletRequest httpServletRequest;

    public Account getCurrentUser(){
        return (Account)httpServletRequest.getSession().getAttribute("currentUser");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

