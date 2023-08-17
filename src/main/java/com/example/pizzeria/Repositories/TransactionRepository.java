package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> getTransactionsByClient_IdOrderByIdDesc(Long clientId, Pageable pageable);
    Page<Transaction> findAllByOrderByIdDesc(Pageable pageable);
}
