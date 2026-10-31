package com.example.transaction_ledger.service;

import com.example.transaction_ledger.entity.Account;
import com.example.transaction_ledger.entity.LedgerTransactions;
import com.example.transaction_ledger.entity.TransactionType;
import com.example.transaction_ledger.exception.AccountNotFoundException;
import com.example.transaction_ledger.exception.DuplicateReferenceException;
import com.example.transaction_ledger.exception.InsufficientFundsException;
import com.example.transaction_ledger.exception.InvalidTransactionException;
import com.example.transaction_ledger.repository.AccountRepository;
import com.example.transaction_ledger.repository.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionService {


    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository){
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }


    @Transactional
    public LedgerTransactions createTransaction(Long accountId, TransactionType type, BigDecimal amount, String description, String referenceId){

        //Lock the account to prevent the concurrent transactions.
        Account account = accountRepository.findByIdForUpdate(accountId).
                orElseThrow(()-> new AccountNotFoundException(accountId));

        // Check the referenceId within this account.
        if(transactionRepository.existsByAccountIdAndReferenceId(accountId, referenceId)){
            throw new DuplicateReferenceException(referenceId);
        }

        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance;
        if(type == TransactionType.CREDIT) {
            newBalance = currentBalance.add(amount);
        }
        else {
            if(currentBalance.compareTo(amount)<0){
                throw new InsufficientFundsException(currentBalance, amount);
            }
            newBalance = currentBalance.subtract(amount);
        }

        // Update the Balance
        account.setBalance(newBalance);

        // Create the Transaction
        LedgerTransactions  transaction = new LedgerTransactions(accountId,type, amount, description, referenceId,newBalance);

        accountRepository.save(account);
        return transactionRepository.save(transaction);

    }

    @Transactional(readOnly = true)
    public Page<LedgerTransactions> getTransactions(Long accountId, TransactionType type, int page, int pageSize){

        // Check account exists or not
        if(!accountRepository.existsById(accountId)){
            throw new AccountNotFoundException(accountId);
        }

        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.by(
                        Sort.Order.desc("createdAt"),
                        Sort.Order.desc("id")
                )
        );

        if (type == null) {
            return transactionRepository.findByAccountId(accountId, pageable);
        }
        return transactionRepository.findByAccountIdAndType(accountId, type, pageable);

    }

}
