package com.example.sports.po;

import com.example.sports.vo.AttendanceVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor

public class Attendance {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "attendance_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private Activity activity;


    @Column(name="order_date")
    private Date orderDate;

    public AttendanceVO toVO(){
        AttendanceVO attendanceVO = new AttendanceVO();
        attendanceVO.setId(this.id);
        attendanceVO.setAccount(this.account.toVO());
        attendanceVO.setActivity(this.activity.toVO());
        attendanceVO.setOrderDate(orderDate);
        return attendanceVO;
    }
}
