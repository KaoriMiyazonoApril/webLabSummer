package com.example.sports.repository;

import com.example.sports.po.Evaluate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluateRepository extends JpaRepository<Evaluate, Integer> {
    Evaluate findByProductIdAndUserId(Integer productId, Integer userId);
}
