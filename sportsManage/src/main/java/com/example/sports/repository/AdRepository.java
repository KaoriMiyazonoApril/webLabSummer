package com.example.sports.repository;

import com.example.sports.po.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad,Integer> {
    Ad findByIdAndProductId(Integer id,Integer productId);
}
