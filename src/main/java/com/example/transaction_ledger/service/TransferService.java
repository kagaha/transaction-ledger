package com.example.transaction_ledger.service;


import com.example.transaction_ledger.dto.request.TransferRequest;
import com.example.transaction_ledger.dto.response.TransactionResponse;
import com.example.transaction_ledger.dto.response.TransferResponse;
import com.example.transaction_ledger.entity.Account;
import com.example.transaction_ledger.entity.LedgerTransactions;
import com.example.transaction_ledger.entity.TransactionType;
import com.example.transaction_ledger.exception.AccountNotFoundException;
import com.example.transaction_ledger.exception.DuplicateReferenceException;
import com.example.transaction_ledger.exception.InsufficientFundsException;
import com.example.transaction_ledger.repository.AccountRepository;
import com.example.transaction_ledger.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransferResponse transferAmount(TransferRequest request){
        Long fromId = request.fromAccountId();
        Long toId = request.toAccountId();
        BigDecimal amount = request.amount();
        String referenceId = request.referenceId();
        String description = request.description();


        if(fromId.equals(toId)){
            throw new IllegalArgumentException("Source and destination accounts must be different.");
        }

        // Lock both accounts in ascending order which will prevents from deadlock risk.
        Long firstId = Math.min(fromId, toId);
        Long secondId = Math.max(fromId, toId);

        Account firstAccount = accountRepository.findByIdForUpdate(firstId)
                .orElseThrow(()-> new AccountNotFoundException(firstId));

        Account secondAccount = accountRepository.findByIdForUpdate(secondId)
                .orElseThrow(()-> new AccountNotFoundException(secondId));

        // Identifying the fromAccount and toAccount to make the correct transaction.
        Account fromAccount = fromId.equals(firstId) ? firstAccount : secondAccount;
        Account toAccount = toId.equals(firstId) ? firstAccount : secondAccount;

        // Prevent the same reference being reused from either account.
        if(transactionRepository.existsByAccountIdAndReferenceId(fromId, referenceId) || transactionRepository.existsByAccountIdAndReferenceId(toId,referenceId)){
            throw  new DuplicateReferenceException(referenceId);
        }

        BigDecimal fromBalance = fromAccount.getBalance();
        BigDecimal toBalance = toAccount.getBalance();

        if(fromBalance.compareTo(amount)<0){
            throw new InsufficientFundsException(fromBalance, amount);
        }

        BigDecimal newFromBalance = fromBalance.subtract(amount);
        BigDecimal newToBalance = toBalance.add(amount);

        //update both account balances
        fromAccount.setBalance(newFromBalance);
        toAccount.setBalance(newToBalance);

        // Create the debit ledger entry.
        LedgerTransactions debit = new LedgerTransactions(
                fromId,
                TransactionType.DEBIT,
                amount,
                request.description(),
                referenceId,
                newFromBalance
        );

        // Create the credit ledger entry.
        LedgerTransactions credit = new LedgerTransactions(
                toId,
                TransactionType.CREDIT,
                amount,
                null,
                referenceId,
                newToBalance
        );

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        transactionRepository.save(debit);
        transactionRepository.save(credit);

        return new TransferResponse(fromId,
                toId,
                amount,
                referenceId,
                "COMPLETED",
                "Transfer completed successfully");
    }
}
