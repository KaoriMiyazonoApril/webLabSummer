package com.example.sports.controller;

import com.example.sports.po.Attendance;
import com.example.sports.service.AttendanceService;
import com.example.sports.vo.AttendanceVO;
import com.example.sports.vo.CommentVO;
import com.example.sports.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PutMapping("/attend")
    public Response attend(@RequestBody AttendanceVO attendance1) {
        return Response.buildSuccess(attendanceService.processOrder(attendance1));
    }

    @DeleteMapping("/cancel")
    public Response cancel(@RequestBody AttendanceVO attendance1) {
        return Response.buildSuccess(attendanceService.cancelOrder(attendance1));
    }
}
