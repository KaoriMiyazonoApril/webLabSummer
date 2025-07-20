package com.example.sports.vo;

import com.example.sports.po.Attendance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

public class AttendanceVO {
    private Integer id;
    private AccountVO account;
    private ActivityVO activity;
    private Date orderDate;

    public Attendance toPO(){
        Attendance attendance = new Attendance();
        attendance.setId(id);
        attendance.setAccount(this.account.toPO());
        attendance.setActivity(this.activity.toPO());
        attendance.setOrderDate(orderDate);
        return attendance;
    }
}
