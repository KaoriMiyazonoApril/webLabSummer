package com.example.sports.service;

import com.example.sports.exception.SportsException;
import com.example.sports.po.Account;
import com.example.sports.repository.AccountRepository;
import com.example.sports.util.SecurityUtil;
import com.example.sports.util.TokenUtil;
import com.example.sports.vo.AccountVO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//()
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenUtil tk;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Boolean ensureUser(Integer userid){
        Account currentUser = securityUtil.getCurrentUser();
        if (currentUser == null) {
            throw SportsException.notLogin();
        }
        if(!accountRepository.findById(currentUser.getId()).get().getRole().equals("Admin")&&!currentUser.getId().equals(userid)){
            throw SportsException.NoAccession();
        }
        return true;
    }

    public AccountVO register(AccountVO accountVO) {
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
        return newUser.toVO();
    }

    public AccountVO getUserByUserid(Integer userId) {
        if(ensureUser(userId)){
            Account account = accountRepository.findById(userId).get();
            account.setPassword("");
            return account.toVO();
        }
        return new AccountVO();
    }


    public Map<String, Object> login(AccountVO a) {

        Long telephone=a.getTelephone();
        String pwd=a.getPassword();
        if(telephone==null || pwd==null)
            throw SportsException.NoEnoughArguments();
        Account ac = accountRepository.findByTelephone(telephone);
        if (ac == null) {
            throw SportsException.WrongTelephone();
        }
        if (!passwordEncoder.matches(pwd, ac.getPassword())) {
            throw SportsException.WrongPassword();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("token",tk.getToken(ac));
        response.put("userId",ac.getId());
        return response;
    }

    public AccountVO update(AccountVO a) {
        Integer userId=a.getId();
        String username=a.getUsername();
        String password=a.getPassword();
        String avatar=a.getAvatar();

        if(ensureUser(userId)){
            Account ac = accountRepository.findById(userId).get();
            if (avatar != null && !avatar.isEmpty()) {
                ac.setAvatar(avatar);
            }
            if (password != null && !password.isEmpty()) {
                // 加密新密码
                ac.setPassword(passwordEncoder.encode(password));
            }
            if (username != null && !username.isEmpty()) {
                ac.setUsername(username);
            }
            accountRepository.save(ac);
            AccountVO t= ac.toVO();
            t.setPassword(null);
            t.setToken(tk.getToken(ac));
            return t;
        }

        throw SportsException.NoAccession();
    }

    public Boolean deleteUser(Integer id){
        if(ensureUser(id)){
            Account a=accountRepository.findById(id).get();
            if(a.getRole().equals("Admin") && !securityUtil.getCurrentUser().getId().equals(id)){
                throw SportsException.NoAccession();
            }
            accountRepository.delete(a);
            return true;
        }
        return false;
    }

    public Boolean setAdmin(Integer id){
        Account ac = accountRepository.findById(securityUtil.getCurrentUser().getId()).get();
        if(ac.getRole().equals("Admin")){
            Account a=accountRepository.findById(id).get();
            a.setRole("Admin");
            accountRepository.save(a);
            return true;
        }
        throw SportsException.NoAccession();
    }

    public List<AccountVO> getAllUser(){
        Account ac = securityUtil.getCurrentUser();
        if(ac.getRole().equals("Admin")){
            return accountRepository.findAll().stream().map(Account::toVO).collect(Collectors.toList());
        }
        throw SportsException.NoAccession();
    }
}

