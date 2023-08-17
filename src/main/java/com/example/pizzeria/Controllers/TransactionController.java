package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.TransactionDTO;
import com.example.pizzeria.Entities.Transaction;
import com.example.pizzeria.Services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/add")
    public TransactionDTO addTransaction(@RequestBody Transaction transaction) {
        return transactionService.addTransaction(transaction);
    }

    @GetMapping("/client/{id}/{pageNumber}/{pageSize}")
    public Page<TransactionDTO> getTransactionsByClientId(@PathVariable("id") Long clientId, @PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        return transactionService.getTransactionsByClientId(clientId, pageNumber, pageSize);
    }

    @GetMapping("/all/{pageNumber}/{pageSize}")
    public Page<TransactionDTO> getAllTransactions(@PathVariable("pageNumber") Integer pageNumber,@PathVariable("pageSize") Integer pageSize) {
        return transactionService.getAllTransactions(pageNumber, pageSize);
    }
}
