package com.smart.expense.transaction.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // auto-generate the id
    private UUID id;

    // from the specifications, we need to associate a transatction with a user
    private String userId;

    private String transactionId;
    private ZonedDateTime date;
    private String merchant;
    private BigDecimal amount;
    private String currency;
    private String category;
}
