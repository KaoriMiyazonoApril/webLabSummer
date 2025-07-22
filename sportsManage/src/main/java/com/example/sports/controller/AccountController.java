package com.example.sports.controller;

import com.example.sports.service.AccountService;
import com.example.sports.vo.AccountVO;
import com.example.sports.vo.Response;
import net.bytebuddy.build.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("")
    public Response getAllUser() {
        return Response.buildSuccess(accountService.getAllUser());
    }

    @GetMapping("/{userId}")
    public Response getUserById(@PathVariable Integer userId) {
        return Response.buildSuccess(accountService.getUserByUserid(userId));
    }

    @PostMapping()
    public Response createUser(@Valid @RequestBody AccountVO accountVO) {
        return Response.buildSuccess(accountService.register(accountVO));
    }

    @PutMapping()
    public Response updateUser(@RequestBody AccountVO a) {
        return Response.buildSuccess(accountService.update(a));
    }


    @PostMapping("/login")
    public Response login(@RequestBody AccountVO a) {
        return Response.buildSuccess(accountService.login(a));
    }

    @DeleteMapping("/deleteUser/{id}")
    public Response deleteUser(@PathVariable(value = "id") Integer id){
        return Response.buildSuccess(accountService.deleteUser(id));
    }

    @PutMapping("/setAdmin/{id}")
    public Response setAdmin(@PathVariable(value="id")Integer id){
        return Response.buildSuccess(accountService.setAdmin(id));
    }
}
