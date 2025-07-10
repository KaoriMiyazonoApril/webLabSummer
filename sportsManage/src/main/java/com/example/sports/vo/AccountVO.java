package com.example.sports.vo;

import com.example.sports.po.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountVO {
    private Integer id;
    private String username;
    private String password;
    private Long phone;
    private String role;
    private String avatar;

    public Account toPO() {
        Account account = new Account();
        account.setId(this.id);
        account.setPhone(this.phone);
        account.setAvatar(this.avatar);
        account.setPassword(this.password);
        account.setUsername(this.username);
        account.setRole(this.role);
        return account;
    }

}
