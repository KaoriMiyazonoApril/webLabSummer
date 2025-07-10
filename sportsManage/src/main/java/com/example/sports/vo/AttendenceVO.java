package com.example.sports.vo;

import com.example.sports.po.Attendence;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

public class AttendenceVO {
    private Integer userId;
    private Integer activityId;
    private Date orderDate;

    public Attendence toPO(){
        Attendence attendence = new Attendence();
        attendence.setUserId(userId);
        attendence.setActivityId(activityId);
        attendence.setOrderDate(orderDate);
        return attendence;
    }
}
