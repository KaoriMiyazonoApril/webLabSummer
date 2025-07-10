package com.example.sports.service;

import com.alipay.api.AlipayApiException;
import com.example.sports.RRVO.ResultOrderPayVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface OrderService {
    ResultOrderPayVO createOrder(Integer orderId);
    void updateOrderStatus(String orderId, String alipayTradeNo, String amount);
    String handleAlipayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException;
}
