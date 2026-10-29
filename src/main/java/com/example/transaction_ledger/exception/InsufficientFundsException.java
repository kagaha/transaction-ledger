package com.example.transaction_ledger.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(BigDecimal balance, BigDecimal requestedAmount) {
        super("Insufficient funds. Available Balance: "+ balance +", requested amount: "+ requestedAmount );
    }
}
