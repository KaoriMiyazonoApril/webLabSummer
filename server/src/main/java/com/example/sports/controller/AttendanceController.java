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

    @DeleteMapping("/cancel/{userId}/{activityId}")
    public Response cancel(@PathVariable(name="userId") Integer userId, @PathVariable(name="activityId") Integer activityId) {
        return Response.buildSuccess(attendanceService.cancelOrder(userId,activityId));
    }

    //活动的参与者,只能得到用户名和头像
    @GetMapping("/member/{activityId}")
    public Response getActivity(@PathVariable("activityId") Integer activityId) {
        return Response.buildSuccess(attendanceService.getByActivityId(activityId));
    }

    //获取个人参与的所有活动
    @GetMapping("personal/{userId}")
    public Response getPersonal(@PathVariable("userId") Integer userId) {
        return Response.buildSuccess(attendanceService.getByUserId(userId));
    }

    @GetMapping("btnType/{userId}/{activityId}")
    public Response getBtnType(@PathVariable("userId") Integer userId,@PathVariable("activityId") Integer activityId) {
        return Response.buildSuccess(attendanceService.getBtnType(userId,activityId));
    }
}
