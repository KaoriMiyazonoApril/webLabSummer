package com.example.sports.po;

import com.example.sports.vo.ProductVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name="title")
    private String title;

    @Column(name="price")
    private Double price;

    @Column(name="rate")
    private Double rate;

    @Column(name="description")
    private String description;

    @Column(name="cover")
    private String cover;

    @Column(name = "detail")
    private String detail;

    @Column(name="category")
    private String category;

    @Column(name = "likes")
    private Integer likes = 0;

    @Column(name="sales")
    private Integer sales;

    @Column(name="score")
    private Double score;

    @Column(name="socre_num")
    private Integer scoreNum;

    public ProductVO toVO(){
        ProductVO p=new ProductVO();
        p.setId(String.valueOf(this.id));
        p.setPrice(this.price);
        p.setRate(this.rate);
        p.setTitle(this.title);
        p.setDescription(this.description);
        p.setCover(this.cover);
        p.setDetail(this.detail);
        p.setCategory(this.category);
        p.setLikes(this.likes);
        p.setSales(this.sales);
        p.setScore(this.score);
        p.setScoreNum(this.scoreNum);
        return p;
    }

}
