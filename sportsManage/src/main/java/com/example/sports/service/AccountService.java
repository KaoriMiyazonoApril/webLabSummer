package com.example.sports.service;

import com.example.sports.exception.SportsException;
import com.example.sports.po.Account;
import com.example.sports.po.Carts;
import com.example.sports.repository.AccountRepository;
import com.example.sports.repository.CartsRepository;
import com.example.sports.util.SecurityUtil;
import com.example.sports.util.TokenUtil;
import com.example.sports.vo.AccountVO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
//()
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenUtil tk;

    @Autowired
    private SecurityUtil securityUtil;

    private PasswordEncoder passwordEncoder;

    private CartsRepository cartsRepository;

    private Boolean ensureUser(Integer userid){
        Account currentUser = securityUtil.getCurrentUser();
        if (currentUser == null) {
            throw SportsException.notLogin();
        }
        if(!currentUser.getRole().equals("Admin")&&!currentUser.getId().equals(userid)){
            throw SportsException.NoAccession();
        }
        return true;
    }

    public String register(AccountVO accountVO) {

        if(accountVO.getUsername()== null ||accountVO.getPassword()==null ||accountVO.getTelephone()==null){
            throw SportsException.NoEnoughArguments();
        }
        if(accountRepository.findByTelephone(accountVO.getTelephone()) != null){
            throw SportsException.phoneAlreadyExists();
        }

        Account newUser = accountVO.toPO();
        newUser.setRole("User");
        newUser.setPassword(passwordEncoder.encode(accountVO.getPassword()));
        accountRepository.save(newUser);
        return "注册成功";
    }

    public AccountVO getUserByUserid(Integer userId) {
        if(ensureUser(userId)){
            Account account = accountRepository.findById(userId).get();
            account.setPassword("");
            return account.toVO();
        }
        return new AccountVO();
    }

    public String login(Long telephone, String pwd) {
        if(telephone==null || pwd==null)
            throw SportsException.NoEnoughArguments();
        Account ac = accountRepository.findByTelephone(telephone);
        if (ac == null) {
            throw SportsException.WrongUsername();
        }

        // 使用matches方法验证密码
        if (!passwordEncoder.matches(pwd, ac.getPassword())) {
            throw SportsException.WrongPassword();
        }

        return tk.getToken(ac);
    }

    public Boolean update(Integer userId,String username, String password, String avatar) {
        if(ensureUser(userId)){
            Account ac = accountRepository.findById(userId).get();

            if (avatar != null) {
                ac.setAvatar(avatar);
            }

            if (password != null) {
                // 加密新密码
                ac.setPassword(passwordEncoder.encode(password));
            }

            if (username != null) {
                ac.setUsername(username);
            }

            accountRepository.save(ac);
            return true;
        }
        return false;
    }

    public Boolean deleteUser(Integer id){
        if(ensureUser(id)){
            Account a=accountRepository.findById(id).get();
            accountRepository.delete(a);
            List<Carts> carts=cartsRepository.findByAccount(a);
            for(Carts c:carts){
                cartsRepository.delete(c);
            }
            return true;
        }
        return false;
    }

    public Boolean setAdmin(Integer id){
        Account ac = securityUtil.getCurrentUser();
        if(ac.getRole().equals("Admin")){
            Account a=accountRepository.findById(id).get();
            a.setRole("Admin");
            return true;
        }
        throw SportsException.NoAccession();
    }
}

