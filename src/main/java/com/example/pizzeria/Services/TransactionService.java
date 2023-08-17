package com.example.pizzeria.Services;


import com.example.pizzeria.DTOs.TransactionDTO;
import com.example.pizzeria.Entities.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {

    TransactionDTO addTransaction(Transaction transaction);
    Page<TransactionDTO> getTransactionsByClientId(Long clientId, Integer pageNumber, Integer pageSize);
    Page<TransactionDTO> getAllTransactions(Integer pageNumber, Integer pageSize);
}
