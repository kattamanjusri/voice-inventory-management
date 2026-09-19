package com.voiceinventory.backend.controller;

import com.voiceinventory.backend.entity.StockTransaction;
import com.voiceinventory.backend.repository.StockTransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5176"})
public class TransactionController {

    private final StockTransactionRepository repository;

    public TransactionController(StockTransactionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<StockTransaction> getTransactions() {
        return repository.findAllByOrderByCreatedAtDesc();
    }
}
