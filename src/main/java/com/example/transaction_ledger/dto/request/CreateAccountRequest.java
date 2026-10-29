package com.example.transaction_ledger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
        @NotBlank
        @Size(max = 100, message = "Account name cannot exceed 100 characters.")
        String name
){}
