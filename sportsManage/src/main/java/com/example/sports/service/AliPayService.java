package com.example.sports.service;

public interface AliPayService {
    String createPaymentForm(String orderId, Double totalAmount, String subject);
}
