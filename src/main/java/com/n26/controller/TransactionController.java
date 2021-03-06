package com.n26.controller;

import com.n26.entity.Transaction;
import com.n26.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.n26.validator.TransactionValidator.validateTransaction;

@RestController
@RequestMapping("transactions")
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        logger.info("Validating " + transaction);
        validateTransaction(transaction);
        logger.info("Creating transaction.");
        service.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTransactions() {
        logger.info("Deleting all transactions.");
        service.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}