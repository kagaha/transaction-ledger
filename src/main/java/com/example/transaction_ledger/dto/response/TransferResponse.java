package com.example.transaction_ledger.dto.response;

import java.math.BigDecimal;

public record TransferResponse(
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount,
        String referenceId,
        String status,
        String message
) {}
