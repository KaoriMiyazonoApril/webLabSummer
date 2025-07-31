package com.example.sports.vo;

import com.example.sports.po.Activity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ActivityVO {

    private Integer id;
    private String name;
    private String image;
    private Date date;
    private String detail;
    private Integer cost;
    private Integer limitCount;

    public Activity toPO(){
        Activity activity = new Activity();
        activity.setId(id);
        activity.setName(name);
        activity.setImage(image);
        activity.setDate(date);
        activity.setDetail(detail);
        activity.setCost(cost);
        activity.setLimitCount(limitCount);
        return activity;
    }
}
