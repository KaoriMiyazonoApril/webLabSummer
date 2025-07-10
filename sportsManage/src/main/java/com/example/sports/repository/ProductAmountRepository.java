package com.example.sports.repository;

import com.example.sports.po.ProductAmount;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductAmountRepository extends JpaRepository<ProductAmount,Integer>{
    ProductAmount findByProductId(Integer productId);
}
