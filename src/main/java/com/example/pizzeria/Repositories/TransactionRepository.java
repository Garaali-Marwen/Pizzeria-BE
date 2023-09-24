package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> getTransactionsByClient_IdOrderByIdDesc(Long clientId, Pageable pageable);
    Page<Transaction> findAllByOrderByIdDesc(Pageable pageable);
    @Query("SELECT COALESCE(SUM(t.order.price), 0) FROM Transaction t WHERE t.date >= CURRENT_DATE AND t.date < :nextDay")
    double getIncomeForActualDay(Date nextDay);
    @Query("SELECT COALESCE(SUM(t.order.price), 0) FROM Transaction t WHERE t.date >= :startOfWeek AND t.date < :endOfWeek")
    double getIncomeForActualWeek(Date startOfWeek, Date endOfWeek);
    @Query("SELECT COALESCE(SUM(t.order.price), 0) FROM Transaction t WHERE t.date >= :startOfMonth AND t.date < :endOfMonth")
    double getIncomeForActualMonth(Date startOfMonth, Date endOfMonth);

    @Query("SELECT t FROM Transaction t WHERE t.date >= CURRENT_DATE AND t.date < :nextDay")
    List<Transaction> getTransactionsForActualDay(Date nextDay);
    @Query("SELECT t FROM Transaction t WHERE t.date >= :startOfWeek AND t.date < :endOfWeek")
    List<Transaction> getTransactionsForActualWeek(Date startOfWeek, Date endOfWeek);
    @Query("SELECT t FROM Transaction t WHERE t.date >= :startOfMonth AND t.date < :endOfMonth")
    List<Transaction> getTransactionsForActualMonth(Date startOfMonth, Date endOfMonth);
}
