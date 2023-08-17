package com.example.pizzeria.DTOs;

import java.util.Date;

public record TransactionDTO(
        Long id,
        Date date,
        OrderDTO order,
        ClientDTO client
) {
}
