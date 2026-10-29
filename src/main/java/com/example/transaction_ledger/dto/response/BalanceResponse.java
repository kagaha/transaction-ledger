package com.example.transaction_ledger.dto.response;

import java.math.BigDecimal;

public record BalanceResponse(
        Long accountId,
        BigDecimal balance
) {
}
