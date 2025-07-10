package com.example.sports.vo;
import com.example.sports.po.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductVO {
    private String id;
    private String title,description,cover,detail;
    private Double price;
    private Double rate;
    private List<SpecificationVO> specifications;
    private String category;
    private Integer likes;
    private Integer sales=0;
    private Double score;
    private Integer scoreNum;
    public Product toPO(){
        Product p=new Product();
        if(id!=null)
            p.setId(Integer.parseInt(this.id));
        p.setTitle(this.title);
        p.setDescription(this.description);
        p.setCover(this.cover);
        p.setDetail(this.detail);
        p.setPrice(this.price);
        p.setRate(this.rate);
        p.setCategory(this.category);
        if (this.likes != null) {
            p.setLikes(this.likes);
        }
        p.setSales(this.sales);
        p.setScore(this.score);
        p.setScoreNum(this.scoreNum);
        return p;
    }
}
