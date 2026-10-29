package com.example.transaction_ledger.service;


import com.example.transaction_ledger.entity.Account;
import com.example.transaction_ledger.exception.AccountNotFoundException;
import com.example.transaction_ledger.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }


    @Transactional
    public Account createAccount(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Account name is required");
        }

        Account account = new Account(name.trim());

        return accountRepository.save(account);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Account getAccount(Long accountId) {

        return accountRepository.findById(accountId)
                .orElseThrow(
                        () -> new AccountNotFoundException(accountId)
                );
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public BigDecimal getBalance(Long accountId){
        Account account = getAccount(accountId);
        return account.getBalance();
    }

}
