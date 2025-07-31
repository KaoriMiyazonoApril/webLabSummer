package com.example.sports.vo;

import com.example.sports.po.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentVO {
    private Integer id;
    private ActivityVO activity;
    private AccountVO account;
    private String detail;
    private Integer score;

    public Comment toPO(){
        Comment comment = new Comment();
        comment.setId(this.id);
        comment.setActivity(this.activity.toPO());
        comment.setAccount(this.account.toPO());
        comment.setDetail(detail);
        comment.setScore(score);
        return comment;
    }
}
