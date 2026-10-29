package com.example.transaction_ledger.controller;



import com.example.transaction_ledger.dto.request.CreateAccountRequest;
import com.example.transaction_ledger.dto.response.AccountResponse;
import com.example.transaction_ledger.dto.response.BalanceResponse;
import com.example.transaction_ledger.entity.Account;
import com.example.transaction_ledger.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.BadAttributeValueExpException;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request){
        Account account = accountService.createAccount(request.name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AccountResponse.from(account));
    }


    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId){
        Account account = accountService.getAccount(accountId);
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Long accountId){
        var balance = accountService.getBalance(accountId);
        return ResponseEntity.ok(new BalanceResponse(accountId, balance));
    }
}
