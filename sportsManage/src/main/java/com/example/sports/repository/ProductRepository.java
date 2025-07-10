package com.example.sports.repository;

import com.example.sports.po.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByCategory(String category);

    List<Product> findByTitleContainingOrDescriptionContainingOrCoverContainingOrDetailContaining(String title, String description, String cover, String detail);

    @Modifying
    @Query("UPDATE ProductAmount pa SET pa.amount = pa.amount - :quantity, pa.frozen = pa.frozen + :quantity " +
            "WHERE pa.productId = :productId AND pa.amount >= :quantity")
    @Transactional
    void lockStock(@Param("productId") Integer productId,
                  @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE ProductAmount pa SET pa.amount = pa.amount + :quantity, pa.frozen = pa.frozen - :quantity " +
            "WHERE pa.productId = :productId AND pa.frozen >= :quantity")
    @Transactional
    void releaseStock(@Param("productId") Integer productId,
                   @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE ProductAmount pa SET pa.frozen = pa.frozen - :quantity " +
            "WHERE pa.productId = :productId AND pa.frozen >= :quantity")
    @Transactional
    void reduceStock(@Param("productId") Integer productId,
                      @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE Product pa SET pa.sales = pa.sales + :quantity " +
            "WHERE pa.id = :productId ")
    @Transactional
    void addSales(@Param("productId") Integer productId,
                     @Param("quantity") Integer quantity);
}