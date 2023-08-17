package com.example.pizzeria.Configuration;

import lombok.Getter;

@Getter
public class CreatePaymentResponse {
    private String clientSecret;

    public CreatePaymentResponse(String clientSecret) {
        this.clientSecret = clientSecret;
    }

}
