package com.example.sports.repository;

import com.example.sports.po.Carts;
import com.example.sports.po.OrderDetail;
import com.example.sports.po.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrder(Orders order);
    List<OrderDetail> findByCarts(Carts cartItem);
}
