package com.example.transaction_ledger.dto.response;

import com.example.transaction_ledger.entity.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
        Long id,
        String name,
        BigDecimal balance,
        LocalDateTime createdAt
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }
}
