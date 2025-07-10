package com.example.sports.repository;

import com.example.sports.po.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecificationRepository extends JpaRepository<Specification,Integer>{
    List<Specification> findByProductId(Integer productId);
}
