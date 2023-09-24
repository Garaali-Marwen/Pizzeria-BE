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

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public double getIncomeForActualDay() {
        Date nextDay = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        return transactionRepository.getIncomeForActualDay(nextDay);
    }

    @Override
    public double getIncomeForActualWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.MONDAY));
        return transactionRepository.getIncomeForActualWeek(
                java.sql.Date.valueOf(startOfWeek),
                java.sql.Date.valueOf(endOfWeek)
        );
    }

    @Override
    public double getIncomeForActualMonth() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());
        return transactionRepository.getIncomeForActualMonth(
                java.sql.Date.valueOf(startOfMonth),
                java.sql.Date.valueOf(endOfMonth)
        );
    }

    @Override
    public List<TransactionDTO> getTransactionsForActualDay() {
        Date nextDay = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        return transactionRepository.getTransactionsForActualDay(nextDay).stream().map(transactionDTOMapper).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionsForActualWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.MONDAY));
        return transactionRepository.getTransactionsForActualWeek(
                java.sql.Date.valueOf(startOfWeek),
                java.sql.Date.valueOf(endOfWeek)
        ).stream().map(transactionDTOMapper).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionsForActualMonth() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());
        return transactionRepository.getTransactionsForActualMonth(
                java.sql.Date.valueOf(startOfMonth),
                java.sql.Date.valueOf(endOfMonth)
        ).stream().map(transactionDTOMapper).collect(Collectors.toList());
    }
}
