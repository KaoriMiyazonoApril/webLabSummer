package com.example.sports.vo;

import com.example.sports.po.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentVO {
    private Integer userId;
    private Integer activityId;
    private String detail;
    private Integer score;

    public Comment toPO(){
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setActivityId(activityId);
        comment.setDetail(detail);
        comment.setScore(score);
        return comment;
    }
}
