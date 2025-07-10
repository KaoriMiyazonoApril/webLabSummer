package com.example.sports.RRVO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResultOrderPayVO {
    private String paymentForm;
    private String orderId;
    private String totalAmount;
    private String paymentMethod;
}
