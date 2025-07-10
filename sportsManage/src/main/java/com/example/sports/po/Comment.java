package com.example.sports.po;
import com.example.sports.vo.CommentVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor

public class Comment {
    @Column(name = "userId")
    private Integer userId;

    @Column(name = "activityId")
    private Integer activityId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "detail")
    private String detail;

    public CommentVO toVO(){
        CommentVO vo = new CommentVO();
        vo.setUserId(userId);
        vo.setActivityId(activityId);
        vo.setScore(score);
        vo.setDetail(detail);
        return vo;
    }
}
