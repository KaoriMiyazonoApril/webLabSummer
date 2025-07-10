package com.example.sports.po;

import com.example.sports.vo.AttendenceVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "attendence")
@Getter
@Setter
@NoArgsConstructor

public class Attendence {
    @Column(name="userId")
    private Integer userId;

    @Column(name="activityId")
    private Integer activityId;

    @Column(name="order_date")
    private Date orderDate;

    public AttendenceVO toVO(){
        AttendenceVO attendenceVO = new AttendenceVO();
        attendenceVO.setUserId(userId);
        attendenceVO.setActivityId(activityId);
        attendenceVO.setOrderDate(orderDate);
        return attendenceVO;
    }
}
