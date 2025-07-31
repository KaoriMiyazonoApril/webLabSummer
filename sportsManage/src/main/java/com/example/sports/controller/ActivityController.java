package com.example.sports.controller;

import com.example.sports.po.Activity;
import com.example.sports.repository.ActivityRepository;
import com.example.sports.service.ActivityService;
import com.example.sports.vo.ActivityVO;
import com.example.sports.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    @Autowired
    private ActivityService  activityService;

    @PostMapping("/create")
    public Response createActivity(@RequestBody ActivityVO a) {
        return Response.buildSuccess(activityService.createActivity(a));
    }

    @DeleteMapping("/delete/{activityId}")
    public Response deleteActivity(@PathVariable(name="activityId") Integer id){
        return  Response.buildSuccess(activityService.deleteActivity(id));
    }

    @PutMapping("/alter")
    public Response alterActivity(@RequestBody ActivityVO a){
        return Response.buildSuccess(activityService.alterActivity(a));
    }

    @GetMapping("/get/{activityId}")
    public Response getActivityById(@PathVariable(name="activityId") Integer id){
        return Response.buildSuccess(activityService.getActivityById(id));
    }

    @GetMapping("/available")
    public Response getActivityAvailable(){
        return Response.buildSuccess(activityService.getAllAvailable());
    }

    @GetMapping("/full")
    public Response getActivityFull(){
        return Response.buildSuccess(activityService.getAllFull());
    }

    @GetMapping("/notAvailable")
    public Response getActivityNotAvailable(){
        return Response.buildSuccess(activityService.getAllNotAvailable());
    }
}
