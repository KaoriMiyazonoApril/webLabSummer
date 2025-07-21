package com.example.sports.service;

import com.example.sports.exception.SportsException;
import com.example.sports.po.Account;
import com.example.sports.po.Activity;
import com.example.sports.po.Attendance;
import com.example.sports.repository.ActivityRepository;
import com.example.sports.repository.AttendanceRepository;
import com.example.sports.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service//确保并发的能够参与活动的服务层
public class AttendanceService {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private AttendanceRepository attendanceRepository;

    private Boolean isAdmin(){
        Account account = securityUtil.getCurrentUser();
        if(account.getRole().equals("Admin")){return true;}
        throw SportsException.NoAccession();
    }

    @Transactional
    public void tryLockStock(Integer activityId) {
        if(isAdmin()){
            Activity activity = activityRepository.findById(activityId).get();
            Date now = new Date();
            if(now.after(activity.getDate())){
                throw SportsException.ActivityAlreadyUnaccessible();
            }
            int updated = activityRepository.lockStock(activityId);//修改的行数
            if (updated == 0) {
                throw SportsException.activityFull();
            }
        }

    }

    @Transactional
    public void releaseLockedStock(Integer activityId) {
        if(isAdmin()){
            activityRepository.releaseStock(activityId);
        }
        throw SportsException.NoAccession();
    }


    public Boolean processOrder(Integer activityId) {
        Account account = securityUtil.getCurrentUser();
        if(account==null){
            throw SportsException.notLogin();
        }
        if(attendanceRepository.findByAccount_IdAndActivity_Id(account.getId(), activityId) != null){
            throw SportsException.ActivityAlreadyJoined();
        }
        tryLockStock(activityId);  // 锁定库存
        Attendance attendance = new Attendance();
        attendance.setAccount(account);
        Activity newActivity = new Activity();
        newActivity.setId(activityId);
        attendance.setActivity(newActivity);
        attendance.setOrderDate(new Date());
        attendanceRepository.save(attendance);
        return true;
    }

    public Boolean cancelOrder(Integer activityId) {
        Account account = securityUtil.getCurrentUser();
        if(account==null){
            throw SportsException.notLogin();
        }
        if(attendanceRepository.findByAccount_IdAndActivity_Id(account.getId(), activityId) == null){
            throw SportsException.ActivityNotJoined();
        }
        releaseLockedStock(activityId); // 恢复库存
        Attendance attendance = attendanceRepository.findByAccount_IdAndActivity_Id(account.getId(), activityId);
        attendanceRepository.delete(attendance);
        return true;
    }
}
