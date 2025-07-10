package com.example.sports.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name="evaluate")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Evaluate {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name="product_id")
    private Integer productId;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="score")
    private Integer score;
}