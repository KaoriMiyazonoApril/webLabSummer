package com.example.sports.service;

import com.example.sports.exception.SportsException;
import com.example.sports.po.Account;
import com.example.sports.po.Activity;
import com.example.sports.po.Attendance;
import com.example.sports.repository.AccountRepository;
import com.example.sports.repository.ActivityRepository;
import com.example.sports.repository.AttendanceRepository;
import com.example.sports.util.SecurityUtil;
import com.example.sports.vo.AccountVO;
import com.example.sports.vo.ActivityVO;
import com.example.sports.vo.AttendanceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service//确保并发的能够参与活动的服务层
public class AttendanceService {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Boolean ensureUser(Integer userId) {
        Account account = securityUtil.getCurrentUser();
        if(account.getRole().equals("Admin")||account.getId().equals(userId)){return true;}
        throw SportsException.NoAccession();
    }

    @Transactional
    public void tryLockStock(Integer activityId) {

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

    @Transactional
    public void releaseLockedStock(Integer activityId) {
            activityRepository.releaseStock(activityId);
    }


    public Boolean processOrder(AttendanceVO attendance1) {
        Integer activityId=attendance1.getActivity().getId();
        Integer userId=attendance1.getAccount().getId();
        if(activityId==null||userId==null){throw SportsException.NoEnoughArguments();}

        Account account = securityUtil.getCurrentUser();
        if(account==null){
            throw SportsException.notLogin();
        }
        if(ensureUser(userId)){
            if(attendanceRepository.findByAccount_IdAndActivity_Id(userId, activityId) != null){
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
        throw SportsException.NoAccession();
    }

    public Boolean cancelOrder(AttendanceVO attendance1) {
        Integer activityId=attendance1.getActivity().getId();
        Integer userId=attendance1.getAccount().getId();
        if(activityId==null||userId==null){throw SportsException.NoEnoughArguments();}

        Account account = securityUtil.getCurrentUser();
        if(account==null){
            throw SportsException.notLogin();
        }
        if(ensureUser(userId)){
            if(attendanceRepository.findByAccount_IdAndActivity_Id(userId, activityId) == null){
                throw SportsException.ActivityNotJoined();
            }
            releaseLockedStock(activityId); // 恢复库存
            Attendance attendance = attendanceRepository.findByAccount_IdAndActivity_Id(account.getId(), activityId);
            attendanceRepository.delete(attendance);
            return true;
        }
        throw SportsException.NoAccession();
    }

    public List<ActivityVO> getByUserId(Integer userId){
        if(ensureUser(userId)){
            List<ActivityVO> ans=new ArrayList<>();
            attendanceRepository.findByAccount_Id(userId).forEach(e-> {
                        ans.add(activityRepository.findById(e.getActivity().getId()).get().toVO());
                    }
            );
            return ans;
        }
        throw SportsException.NoAccession();
    }

    public List<AccountVO> getByActivityId(Integer activityId){
        List<AccountVO> ans=new ArrayList<>();
        attendanceRepository.findByActivity_Id(activityId).forEach(e-> {
            Integer userId = e.getAccount().getId();
            AccountVO vo = accountRepository.findById(userId).get().toVO();
            vo.setTelephone(null);
            vo.setPassword(null);
            vo.setId(null);
            vo.setRole(null);
            ans.add(vo);
        });
        return ans;
    }
}
