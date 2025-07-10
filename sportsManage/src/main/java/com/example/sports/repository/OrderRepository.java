package com.example.sports.repository;

import com.example.sports.po.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface OrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByStatusAndCreateTimeBefore(String status, LocalDateTime createTime);
}
