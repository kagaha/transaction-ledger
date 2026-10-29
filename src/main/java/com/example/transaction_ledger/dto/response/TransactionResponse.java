package com.example.transaction_ledger.dto.response;

import com.example.transaction_ledger.entity.LedgerTransactions;
import com.example.transaction_ledger.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long accountId,
        TransactionType type,
        BigDecimal amount,
        String description,
        String referenceId,
        BigDecimal balanceAfterTransaction
) {
    public static TransactionResponse from(
            LedgerTransactions transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getReferenceId(),
                transaction.getBalanceAfterTransaction()
        );
    }
}
