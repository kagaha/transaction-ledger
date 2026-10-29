package com.example.transaction_ledger.repository;


import com.example.transaction_ledger.entity.LedgerTransactions;
import com.example.transaction_ledger.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<LedgerTransactions, Long> {

    boolean existsByAccountIdAndReferenceId(Long accountId, String referenceId);

    Page<LedgerTransactions> findByAccountId(Long accountId, Pageable pageable);

    Page<LedgerTransactions> findByAccountIdAndType(Long accountId, TransactionType type, Pageable pageable);
}
