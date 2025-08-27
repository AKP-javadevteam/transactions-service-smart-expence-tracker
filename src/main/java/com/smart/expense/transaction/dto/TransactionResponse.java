package com.smart.expense.transaction.dto;

import com.smart.expense.transaction.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private List<Transaction> transactions;
}
