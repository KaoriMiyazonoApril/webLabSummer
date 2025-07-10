package com.example.sports.serviceImpl;

import com.example.sports.exception.SportsException;
import com.example.sports.po.Account;
import com.example.sports.po.Carts;
import com.example.sports.po.FavoriteProduct;
import com.example.sports.repository.AccountRepository;
import com.example.sports.repository.CartsRepository;
import com.example.sports.repository.FavorRepository;
import com.example.sports.service.AccountService;
import com.example.sports.util.TokenUtil;
import com.example.sports.vo.AccountVO;
import com.example.sports.vo.FavoriteProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    FavorRepository favorRepository;
    @Autowired
    TokenUtil tk;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartsRepository cartsRepository;

    @Override
    public String register(AccountVO accountVO) {
        Account account = accountRepository.findByUsername(accountVO.getUsername());
        if (account != null) {
            throw SportsException.DuplicateName();
        }
        Account newUser = accountVO.toPO();
        newUser.setPassword(passwordEncoder.encode(accountVO.getPassword()));
        accountRepository.save(newUser);
        return "注册成功";
    }

    @Override
    public AccountVO getUserByUsername(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw SportsException.WrongUsername();
        }
        return account.toVO();
    }

    @Override
    public String login(String username, String pwd) {
        if(username==null || pwd==null)
            throw SportsException.NoEnoughArguments();
        Account ac = accountRepository.findByUsername(username);
        if (ac == null) {
            throw SportsException.WrongUsername();
        }

        // 使用matches方法验证密码
        if (!passwordEncoder.matches(pwd, ac.getPassword())) {
            throw SportsException.WrongPassword();
        }

        return tk.getToken(ac);
    }

    @Override
    public Boolean update(String username, String password, String name, String avatar, String telephone, String email, String location) {
        Account ac = accountRepository.findByUsername(username);
        if (ac == null) {
            throw SportsException.WrongUsername();
        }
        if (name != null) {
            ac.setName(name);
        }
        if (avatar != null) {
            ac.setAvatar(avatar);
        }
        if (telephone != null) {
            ac.setTelephone(telephone);
        }
        if (email != null) {
            ac.setEmail(email);
        }
        if (location != null) {
            ac.setLocation(location);
        }

        if (password != null) {
            // 加密新密码
            ac.setPassword(passwordEncoder.encode(password));
        }

        accountRepository.save(ac);
        return true;
    }

    @Override
    public Boolean deleteUser(Integer id){
        Account a=accountRepository.findById(id).get();
        accountRepository.delete(a);
        List<Carts> carts=cartsRepository.findByAccount(a);
        for(Carts c:carts){
            cartsRepository.delete(c);
        }
        return true;
    }

    @Override
    public Boolean addFavor(FavoriteProductVO f){
        if(f.getUserId()==null || f.getProductId()==null)
            throw SportsException.NoEnoughArguments();
        if(favorRepository.findByUserIdAndProductId(f.getUserId(),f.getProductId())!=null)
            throw SportsException.DuplicateFavor();
        favorRepository.save(f.toPO());
        return true;
    }

    @Override
    public List<Integer> findFavor(Integer userId){
        List<FavoriteProduct> ls=favorRepository.findByUserId(userId);
        List<Integer> ans=new ArrayList<>();
        for(FavoriteProduct f:ls){
            ans.add(f.getProductId());
        }
        return ans;
    }

    @Override
    public Boolean deleteFavor(FavoriteProductVO f){
        if(f.getUserId()==null || f.getProductId()==null)
            throw SportsException.NoEnoughArguments();
        FavoriteProduct fv=favorRepository.findByUserIdAndProductId(f.getUserId(),f.getProductId());
        if(fv==null)
            throw SportsException.FavorNotFound();
        favorRepository.delete(fv);
        return true;
    }
}

