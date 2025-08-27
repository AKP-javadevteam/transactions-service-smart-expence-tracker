package com.smart.expense.transaction.repository;

import com.smart.expense.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserIdAndDateBetween(String userId, ZonedDateTime start, ZonedDateTime end);
}
