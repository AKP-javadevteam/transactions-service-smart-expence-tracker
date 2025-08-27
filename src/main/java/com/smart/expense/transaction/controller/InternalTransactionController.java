package com.smart.expense.transaction.controller;

import com.smart.expense.transaction.dto.TransactionResponse;
import com.smart.expense.transaction.model.Transaction;
import com.smart.expense.transaction.repository.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("internal/transactions-by-user")
public class InternalTransactionController {

    private final TransactionRepository transactionRepository;

    public InternalTransactionController(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<TransactionResponse> getTransactions(@PathVariable String userId, @RequestParam String month, Authentication authentication) {
        System.out.printf("Fetching transactions for userId: %s and month: %s%n", userId, month);

        String authenticatedUserId = authentication.getName();

        if (!authenticatedUserId.equals(userId)){
            throw new SecurityException("Access Denied: You cannot access another users transactions. ");
        }

        // --- MOCKED DATA ---
        // This is a placeholder until the db is implemented
        // it unblocks the Forecasts & Reports service immediately.
        // List<Transaction> mockTransactions = List.of(
        //        new Transaction("txn_1", ZonedDateTime.parse("2025-09-01T10:00:00Z"), "Supermarkt Berlin", new BigDecimal("-55.45"), "EUR", "Groceries"),
        //        new Transaction("txn_2", ZonedDateTime.parse("2025-09-02T18:30:00Z"), "BVG Ticket", new BigDecimal("-2.90"), "EUR", "Transport"),
        //        new Transaction("txn_3", ZonedDateTime.parse("2025-09-05T20:00:00Z"), "Cinema", new BigDecimal("-15.00"), "EUR", "Entertainment")
        //);

        // --- Replace MOCKED DATA with REAL DATABASE LOGIC ---

        YearMonth yearMonth = YearMonth.parse(month);
        ZonedDateTime start = yearMonth.atDay(1).atStartOfDay(java.time.ZoneOffset.UTC);
        ZonedDateTime end = yearMonth.atEndOfMonth().atTime(23,59,59).atZone(java.time.ZoneOffset.UTC);

        List<Transaction> userTransactions = transactionRepository.findByUserIdAndDateBetween(userId, start, end);

        if (userTransactions.isEmpty()){
            return ResponseEntity.ok(new TransactionResponse((List.of())));
        }

        TransactionResponse response = new TransactionResponse(userTransactions);
        return ResponseEntity.ok(response);
    }
}
