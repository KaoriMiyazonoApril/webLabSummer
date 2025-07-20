package com.example.sports.controller;

import com.example.sports.service.AccountService;
import com.example.sports.vo.AccountVO;
import com.example.sports.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;


    @GetMapping("/{userId}")
    public Response<AccountVO> getUser(@PathVariable Integer userId) {
        return Response.buildSuccess(accountService.getUserByUserid(userId));
    }

    @PostMapping()
    public Response<String> createUser(@Valid @RequestBody AccountVO accountVO) {
        return Response.buildSuccess(accountService.register(accountVO));
    }

    @PutMapping()
    // 在更新方法中添加role参数
    public Response updateUser(@RequestBody AccountVO a) {
        return Response.buildSuccess(accountService.update(a.getId(),a.getUsername(), a.getPassword(), a.getAvatar()));
    }


    @PostMapping("/login")
    public Response login(@RequestBody AccountVO a) {
        return Response.buildSuccess(accountService.login(a.getTelephone(), a.getPassword()));
    }

    @DeleteMapping("/deleteUser/{id}")
    public Response deleteUser(@PathVariable(value = "id") Integer id){
        return Response.buildSuccess(accountService.deleteUser(id));
    }

}
