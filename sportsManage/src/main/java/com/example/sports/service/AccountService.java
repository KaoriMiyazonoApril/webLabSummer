package com.example.sports.service;

import com.example.sports.vo.AccountVO;
import com.example.sports.vo.FavoriteProductVO;

import java.util.List;

public interface AccountService {

    String register(AccountVO accountVO);
    AccountVO getUserByUsername(String username);
    String login(String username,String password);
    Boolean update(String username,String password,String name,String avatar,String telephone,String email,String location);
    Boolean deleteUser(Integer id);
    Boolean addFavor(FavoriteProductVO f);
    List<Integer> findFavor(Integer userId);
    Boolean deleteFavor(FavoriteProductVO f);
}
