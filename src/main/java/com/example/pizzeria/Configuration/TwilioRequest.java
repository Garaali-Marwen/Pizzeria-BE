package com.example.pizzeria.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TwilioRequest {

    private String phoneNumber;
    private String message;
}
