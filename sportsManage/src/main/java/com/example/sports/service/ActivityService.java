package com.example.sports.service;

import com.example.sports.exception.SportsException;
import com.example.sports.po.Account;
import com.example.sports.po.Activity;
import com.example.sports.repository.ActivityRepository;
import com.example.sports.util.SecurityUtil;
import com.example.sports.vo.AccountVO;
import com.example.sports.vo.ActivityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private SecurityUtil securityUtil;

    private Boolean isAdmin(){
        Account currentUser=securityUtil.getCurrentUser();
        if(currentUser==null){
            throw SportsException.notLogin();
        }
        if(currentUser.getRole()!="Admin"){
            throw SportsException.NoAccession();
        }
        return true;
    }

    public ActivityVO createActivity(ActivityVO a){
        if(isAdmin()){
            if(a.getName()==null||a.getLimit()==null||a.getDate()==null){
                throw SportsException.NoEnoughArguments();
            }
            if(a.getCost()==null){
                a.setCost(0);
            }

            return activityRepository.save(a.toPO()).toVO();
        }
        return new ActivityVO();
    }

    public boolean deleteActivity(Integer Id){
        if(isAdmin()){
            Activity a=activityRepository.findById(Id).get();
            activityRepository.delete(a);
            return true;
        }
        return false;
    }

    public ActivityVO alterActivity(ActivityVO a){
        if(isAdmin()){
            if(a.getId()==null){
                throw SportsException.NoEnoughArguments();
            }
            Activity a1=activityRepository.findById(a.getId()).get();
            if(a.getDetail()!=null){
                a1.setDetail(a.getDetail());
            }
            if(a.getLimit()!=null){
                a1.setLimit(a.getLimit());
            }
            if(a.getCost()!=null){
                a1.setCost(a.getCost());
            }
            if(a.getDate()!=null){
                a1.setDate(a.getDate());
            }
            return a1.toVO();
        }
        return new ActivityVO();
    }

    public ActivityVO getActivityById(Integer Id){
        return activityRepository.findById(Id).get().toVO();
    }
    //没过期且没有满员的活动
    public List<ActivityVO> getAllAvailable(){
        List<Activity> all=activityRepository.findAll();
        List<ActivityVO> res=new ArrayList<ActivityVO>();
        Date today= new Date(System.currentTimeMillis());

        for(Activity a:all){
            if(a.getDate().after(today)&&a.getLimit()>0){
                res.add(a.toVO());
            }
        }
        return res;
    }
    //没过期但满员了
    public List<ActivityVO> getAllAvailableNotNow(){
        List<Activity> all=activityRepository.findAll();
        List<ActivityVO> res=new ArrayList<ActivityVO>();
        Date today= new Date(System.currentTimeMillis());

        for(Activity a:all){
            if(a.getDate().after(today)&&a.getLimit()==0){
                res.add(a.toVO());
            }
        }
        return res;
    }
    //过期的活动
    public List<ActivityVO> getActivityNotAvailable(){
        List<Activity> all=activityRepository.findAll();
        List<ActivityVO> res=new ArrayList<ActivityVO>();
        Date today= new Date(System.currentTimeMillis());

        for(Activity a:all){
            if(a.getDate().before(today)){
                res.add(a.toVO());
            }
        }
        return res;
    }

}
