package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.Mappers.TransactionDTOMapper;
import com.example.pizzeria.DTOs.TransactionDTO;
import com.example.pizzeria.Entities.Transaction;
import com.example.pizzeria.Repositories.TransactionRepository;
import com.example.pizzeria.Services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionDTOMapper transactionDTOMapper;

    @Override
    public TransactionDTO addTransaction(Transaction transaction) {
        transaction.setDate(new Date());
        return transactionDTOMapper.apply(transactionRepository.save(transaction));
    }

    @Override
    public Page<TransactionDTO> getTransactionsByClientId(Long clientId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return transactionRepository.getTransactionsByClient_IdOrderByIdDesc(clientId, pageable).map(transactionDTOMapper);
    }

    @Override
    public Page<TransactionDTO> getAllTransactions(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return transactionRepository.findAllByOrderByIdDesc(pageable).map(transactionDTOMapper);
    }
}
