package com.example.sports.service;

import com.example.sports.RRVO.AddCartResultVO;
import com.example.sports.RRVO.CartResultVO;
import com.example.sports.RRVO.CheckoutRequestVO;
import com.example.sports.RRVO.CheckoutResultVO;

public interface CartsService {
    AddCartResultVO addCart(String productId, Integer quantity);
    String deleteCart(String productId);
    Boolean updateCart(String cartItemId, Integer quantity);
    CartResultVO getCart();
    CheckoutResultVO checkout(CheckoutRequestVO request);
    CheckoutResultVO cancelOrder(String orderId);
}
