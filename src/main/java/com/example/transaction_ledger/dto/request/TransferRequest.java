package com.example.transaction_ledger.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequest(

        @NotNull(message =" Source account ID is required")
        Long fromAccountId,

        @NotNull(message = "Destination account ID is required")
        Long toAccountId,

        @NotNull(message = "Transfer amount is required")
        @DecimalMin(
                value = "0.00",
                inclusive = false,
                message = "Transfer amount must be greater than zero"
        )
        BigDecimal amount,

        @NotNull(message = "Reference Id is required")
        @Size(max = 100, message = "Reference ID cannot exceed 100 characters")
        String referenceId,

        String description
) {
}
