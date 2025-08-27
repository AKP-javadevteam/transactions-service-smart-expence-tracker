package com.smart.expense.transaction.controller;

import com.opencsv.CSVReader;
import com.smart.expense.transaction.model.Transaction;
import com.smart.expense.transaction.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class PublicTransactionController {

    private final TransactionRepository transactionRepository;

    public PublicTransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    // --- Your method for BULK CSV import ---
    @PostMapping("/import/csv")
    public ResponseEntity<Map<String, String>> importTransactionsFromCsv(@RequestParam("file") MultipartFile file, Authentication authentication) {
        // TODO: Get userId from JWT
       //String userId = "alice";

        String userId = authentication.getName();


        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }

        List<Transaction> transactionsToSave = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String header = reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String cleanLine = line.trim();

                if (cleanLine.startsWith("\"")  && cleanLine.endsWith("\"")){
                    cleanLine = cleanLine.substring(1, cleanLine.length() - 1);
                }
                String[] parts = cleanLine.split(",");

                // Assuming CSV format: 0:Date, 1:Merchant, 2:Amount, 3:Currency, 4:Category
                Transaction transaction = new Transaction();
                transaction.setUserId(userId);
                transaction.setTransactionId("txn_" + UUID.randomUUID().toString().substring(0, 8));
                transaction.setDate(ZonedDateTime.parse(parts[0].trim()));
                transaction.setMerchant(parts[1].trim());
                transaction.setAmount(new BigDecimal(parts[2].trim()));
                transaction.setCurrency(parts[3].trim());
                transaction.setCategory(parts[4].trim());
                transactionsToSave.add(transaction);
            }
        } catch (Exception e) {
            // Handle exceptions for invalid file format
            return ResponseEntity.badRequest().body(Map.of("error", "InvalidFileFormat", "message", e.getMessage()));
        }
        transactionRepository.saveAll(transactionsToSave);

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

