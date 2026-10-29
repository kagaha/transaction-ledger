package com.example.transaction_ledger.controller;


import com.example.transaction_ledger.dto.request.CreateTransactionRequest;
import com.example.transaction_ledger.dto.response.TransactionHistoryResponse;
import com.example.transaction_ledger.dto.response.TransactionResponse;
import com.example.transaction_ledger.entity.LedgerTransactions;
import com.example.transaction_ledger.entity.TransactionType;
import com.example.transaction_ledger.service.AccountService;
import com.example.transaction_ledger.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Validated
@RequestMapping("/api/v1/accounts/{accountId}/transactions")
public class TransactionController {


    private final AccountService accountService;
    private final TransactionService transactionService;

    public TransactionController(AccountService accountService, TransactionService transactionService){
        this.accountService= accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@PathVariable Long accountId, @Valid @RequestBody CreateTransactionRequest request){

        LedgerTransactions transaction = transactionService.createTransaction(accountId,request.type(), request.amount(),request.description(), request.referenceId());

        TransactionResponse response = TransactionResponse.from(transaction);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);

    }


    @GetMapping
    public ResponseEntity<TransactionHistoryResponse> getTransactionHistory(@PathVariable Long accountId,
                                                                            @RequestParam(required = false)TransactionType type,
                                                                            @RequestParam(defaultValue = "0") @Min(value =0,message ="Page must be zero or greater")
                                                                                int page,

                                                                            @RequestParam(defaultValue = "10")
                                                                                @Min(value=1, message ="Page size should be at least 1")
                                                                                @Max(value =100, message ="page size cannot exceed 100")
                                                                                int pageSize)
    {
        Page<LedgerTransactions> transactionsPage = transactionService.getTransactions(accountId,type,page,pageSize);
        TransactionHistoryResponse response = TransactionHistoryResponse.from(accountId, transactionsPage);

        return ResponseEntity.ok(response);
    }
}
