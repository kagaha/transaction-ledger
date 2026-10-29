package com.example.transaction_ledger.dto.response;

import com.example.transaction_ledger.entity.LedgerTransactions;
import org.springframework.data.domain.Page;
import java.util.List;

public record TransactionHistoryResponse(
        Long accountId,
        List<TransactionResponse> transactions,
        PaginationMetadata pagination
) {

    public record PaginationMetadata(
            int page,
            int pageSize,
            long totalElements,
            int totalPages,
            boolean hasNext,
            boolean hasPrevious
    ) {
    }

    public static TransactionHistoryResponse from(
            Long accountId,
            Page<LedgerTransactions> transactionPage) {

        List<TransactionResponse> transactions =
                transactionPage.getContent()
                        .stream()
                        .map(transaction -> new TransactionResponse(
                                transaction.getId(),
                                transaction.getAccountId(),
                                transaction.getType(),
                                transaction.getAmount(),
                                transaction.getDescription(),
                                transaction.getReferenceId(),
                                transaction.getBalanceAfterTransaction()
                        ))
                        .toList();

        PaginationMetadata pagination = new PaginationMetadata(
                transactionPage.getNumber(),
                transactionPage.getSize(),
                transactionPage.getTotalElements(),
                transactionPage.getTotalPages(),
                transactionPage.hasNext(),
                transactionPage.hasPrevious()
        );

        return new TransactionHistoryResponse(
                accountId,
                transactions,
                pagination
        );
    }
}
