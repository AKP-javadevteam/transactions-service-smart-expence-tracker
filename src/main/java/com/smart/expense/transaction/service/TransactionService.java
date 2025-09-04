package com.smart.expense.transaction.service;

import com.smart.expense.transaction.model.Transaction;
import com.smart.expense.transaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional // data consistance annotation
    public void importTransactionsFromCsv(MultipartFile file, String userId) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        List<Transaction> transactionsToSave = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String header = reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String cleanLine = line.trim();

                if (cleanLine.startsWith("\"") && cleanLine.endsWith("\"")) {
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

            transactionRepository.saveAll(transactionsToSave);


        }

    }
}
