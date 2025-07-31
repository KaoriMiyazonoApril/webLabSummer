package com.example.sports.po;
import com.example.sports.vo.CommentVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private Activity activity;

    @Column(name = "score")
    private Integer score;

    @Column(name = "detail")
    private String detail;

    public CommentVO toVO(){
        CommentVO vo = new CommentVO();
        vo.setAccount(this.account.toVO());
        vo.setActivity(this.activity.toVO());
        vo.setScore(score);
        vo.setDetail(detail);
        return vo;
    }
}
