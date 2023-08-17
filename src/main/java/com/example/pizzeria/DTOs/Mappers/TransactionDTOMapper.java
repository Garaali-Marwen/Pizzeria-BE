package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.TransactionDTO;
import com.example.pizzeria.Entities.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@AllArgsConstructor
@Service
public class TransactionDTOMapper implements Function<Transaction, TransactionDTO> {

    private final OrderDTOMapper orderDTOMapper;
    private final ClientDTOMapper clientDTOMapper;

    @Override
    public TransactionDTO apply(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getDate(),
                orderDTOMapper.apply(transaction.getOrder()),
                clientDTOMapper.apply(transaction.getClient())
        );
    }
}
