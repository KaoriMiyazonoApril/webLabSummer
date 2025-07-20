package com.example.sports.po;

import com.example.sports.vo.AccountVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@Entity

public class Account {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "telephone")
    private Long telephone;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "role")
    private String role;

    public AccountVO toVO(){
        AccountVO accountVO = new AccountVO();
        accountVO.setId(this.id);
        accountVO.setUsername(this.username);
        accountVO.setPassword(this.password);
        accountVO.setTelephone(this.telephone);
        accountVO.setAvatar(this.avatar);
        accountVO.setRole(this.role);  // 新增
        return accountVO;
    }
}
