package com.example.sports.po;

import com.example.sports.vo.ActivityVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "activity")
@Getter
@Setter
@NoArgsConstructor
@Entity

public class Activity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendance;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "date")
    private Date date;

    @Column(name = "detail")
    private String detail;

    @Column(name="cost")
    private Integer cost;

    @Column(name="limitCount")
    private Integer limitCount;//剩下的名额

    public ActivityVO toVO(){
        ActivityVO vo = new ActivityVO();
        vo.setId(id);
        vo.setName(name);
        vo.setImage(image);
        vo.setDate(date);
        vo.setDetail(detail);
        vo.setCost(cost);
        vo.setLimitCount(limitCount);
        return vo;
    }
}
