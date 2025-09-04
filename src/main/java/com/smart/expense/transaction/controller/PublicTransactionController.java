package com.smart.expense.transaction.controller;

import com.smart.expense.transaction.model.Transaction;
import com.smart.expense.transaction.repository.TransactionRepository;
import com.smart.expense.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class PublicTransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public PublicTransactionController(TransactionService transactionService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }
    // --- Your method for BULK CSV import ---
    @PostMapping("/import/csv")
    public ResponseEntity<Map<String, String>> importTransactionsFromCsv(@RequestParam("file") MultipartFile file, Authentication authentication) {
        // TODO: Get userId from JWT
       //String userId = "alice";

        String userId = authentication.getName();

        try {
            transactionService.importTransactionsFromCsv(file, userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "InvalidFileFormat", "message", e.getMessage()));
        }

        // Return 202 Accepted as per the API spec
        return new ResponseEntity<>(
                Map.of(
                        "status", "processing",
                        "message", "File accepted and is being processed. ",
                        "fieldId", UUID.randomUUID().toString()
                ),
                HttpStatus.ACCEPTED
        );

    }
    // --- This method for a SINGLE transaction ---
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction newTransaction, Authentication authentication){
        //  get userId from JWT from auth-service
        String userId = authentication.getName();
        newTransaction.setUserId(userId);

        /*
        if (newTransaction.getUserId() == null ) {
            newTransaction.setUserId("alice"); }

         */

        Transaction savedTransaction = transactionRepository.save(newTransaction);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);


    }


}

