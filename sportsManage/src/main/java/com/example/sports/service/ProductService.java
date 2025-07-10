package com.example.sports.service;

import com.example.sports.exception.SportsException;
import com.example.sports.po.*;
import com.example.sports.repository.*;
import com.example.sports.util.SecurityUtil;
import com.example.sports.vo.CommentVO;
import com.example.sports.vo.ProductAmountVO;
import com.example.sports.vo.ProductVO;
import com.example.sports.vo.SpecificationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductAmountRepository productAmountRepository;
    @Autowired
    SpecificationRepository specificationRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CartsRepository cartsRepository;
    @Autowired
    EvaluateRepository evaluateRepository;
    @Autowired
    private SecurityUtil securityUtil;

    public List<ProductVO> getAllProduct(){//完成了
        List<Product> ls=productRepository.findAll();
        List<ProductVO> ans=new ArrayList<>();

        for(Product tem:ls){
            ProductVO t=tem.toVO();
            List<SpecificationVO> sp=new ArrayList<>();
            for(Specification s:specificationRepository.findByProductId(tem.getId())){
                sp.add(s.toVO());
            }
            t.setSpecifications(sp);
            ans.add(t);
        }
        return ans;
    }

    public ProductVO getProduct(Integer id){//完成了
        ProductVO p=productRepository.findById(id).get().toVO();
        List<SpecificationVO> sp=new ArrayList<>();
        for(Specification s: specificationRepository.findByProductId(id)){
            sp.add(s.toVO());
        }
        p.setSpecifications(sp);
        return p;
    }

    public ProductAmountVO getAmount(Integer id){//完成了
        ProductAmount p=productAmountRepository.findByProductId(id);
        if(p==null){
            throw SportsException.ProductNotFound();
        }
        return p.toVO();
    }

    public String deleteProduct(Integer id){//完成了
        productRepository.delete(productRepository.findById(id).get());
        productAmountRepository.delete(productAmountRepository.findByProductId(id));

        for(Specification tem:specificationRepository.findByProductId(id))
            specificationRepository.delete(tem);
        List<Carts> carts= cartsRepository.findByProductId(id);
        for(Carts c:carts){
            cartsRepository.delete(c);
        }
        return "删除成功";
    }

    public ProductVO createProduct(ProductVO p){//完成了
        if(p.getTitle() ==null || p.getPrice() ==null || p.getRate() ==null)
            throw SportsException.NoEnoughArguments();
        Product tem=p.toPO();
        productRepository.save(tem);//据说这时候id就有了
        //System.out.println(tem.getId());
        productAmountRepository.save(new ProductAmount(tem.getId(),0,0));
        if(p.getSpecifications()!=null){
            for(SpecificationVO s:p.getSpecifications()){
                Specification sp=s.toPO();
                sp.setProductId(tem.getId());
                specificationRepository.save(sp);
            }
        }
        p.setId(tem.getId().toString());
        return p;
    }

    public String updateProduct(ProductVO p) {//完成了
        if(p.getId()==null)
            throw SportsException.NoEnoughArguments();

        Product tem=productRepository.findById(Integer.parseInt(p.getId())).get();

        if(p.getTitle()!=null)
            tem.setTitle(p.getTitle());
        if(p.getPrice()!=null)
            tem.setPrice(p.getPrice());
        if(p.getRate()!=null)
            tem.setRate(p.getRate());
        if(p.getDescription()!=null)
            tem.setDescription(p.getDescription());
        if(p.getCover()!=null)
            tem.setCover(p.getCover());
        if(p.getDetail()!=null)
            tem.setDetail(p.getDetail());

        productRepository.save(tem);
        for(Specification s:specificationRepository.findByProductId(Integer.parseInt(p.getId()))){
            specificationRepository.delete(s);
        }
        if(p.getSpecifications()!=null)
            for(SpecificationVO s:p.getSpecifications()){
                specificationRepository.save(s.toPO());
            }
        return "更新成功";
    }

    public String updateAmount(Integer id,Integer amount){//完成
        if(amount<0)
            throw SportsException.InvaildProductAmount();
        ProductAmount p=productAmountRepository.findByProductId(id);
        if(p==null)
            throw SportsException.ProductNotFound();
        p.setAmount(amount);
        productAmountRepository.save(p);
        return "调整库存成功";
    }

    public List<ProductVO> searchForSomething(String something){
        List<Product> ls=productRepository.findByTitleContainingOrDescriptionContainingOrCoverContainingOrDetailContaining(something,something,something,something);
        List<ProductVO> ans=new ArrayList<>();
        for(Product p:ls){
            ans.add(p.toVO());
        }
        return ans;
    }

    public List<ProductVO> getByCategory(String cate){
        List<Product> ls=productRepository.findByCategory(cate);
        List<ProductVO> ans=new ArrayList<>();
        for(Product p:ls){
           ans.add(p.toVO());
        }
        return ans;
    }

    public String addComment(CommentVO c){
        if(c.getUserId()==null || c.getProductId()==null)
            throw SportsException.NoEnoughArguments();
        Comment cm=commentRepository.findByUserIdAndProductId(c.getUserId(),c.getProductId());
        if(cm==null){
            commentRepository.save(c.toPO());
            return "添加评论成功";
        }
        else{
            cm.setDetail(c.getDetail());
            commentRepository.save(cm);
            return "修改评论成功";
        }
    }

    public List<CommentVO> findByProductId(Integer id){
        List<Comment> ls=commentRepository.findByProductId(id);
        List<CommentVO> ans=new ArrayList<>();
        for(Comment c:ls){
            ans.add(c.toVO());
        }
        return ans;
    }

    public String deleteComment(CommentVO c){
        if(c.getUserId()==null || c.getProductId()==null)
            throw SportsException.NoEnoughArguments();
        Comment cm=commentRepository.findByUserIdAndProductId(c.getUserId(),c.getProductId());
        if(cm!=null){
            commentRepository.delete(cm);
            return "删除成功";
        }
        else{
            throw SportsException.CommentNotFound();
        }
    }

    public String updateComment(CommentVO c,Integer id){
        if(c.getUserId()==null || c.getProductId()==null)
            throw SportsException.NoEnoughArguments();
        Comment cm=commentRepository.findById(id).get();
        cm.setDetail(c.getDetail());
        commentRepository.save(cm);
        return "修改成功";
    }
    public ProductVO likeProduct(int productId) {
        Product product = (Product)this.productRepository.findById(productId).orElseThrow(() -> new RuntimeException("商品不存在"));
        product.setLikes(product.getLikes() + 1);
        this.productRepository.save(product);
        return product.toVO();
    }
    public String evaluate(Integer id,Integer score){
        Account account = securityUtil.getCurrentUser();
        if(account==null){
            throw SportsException.notLogin();
        }
        Product p=productRepository.findById(id).get();
        p.setScore((p.getScore()*p.getScoreNum()+score)/(p.getScoreNum()*1.0));
        p.setScoreNum(p.getScoreNum()+1);
        productRepository.save(p);
        Evaluate evaluate = evaluateRepository.findByProductIdAndUserId(id, account.getId());
        if (evaluate == null) {
            evaluate = new Evaluate();
            evaluate.setProductId(id);
            evaluate.setUserId(account.getId());
        }
        evaluate.setScore(score);
        evaluateRepository.save(evaluate);
        return "评价成功";
    }
}



