package com.example.transaction_ledger.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="transactions")
@Getter
@Setter
@NoArgsConstructor
public class LedgerTransactions {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(name="account_id", nullable = false, updatable = false)
    private Long accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(updatable=false)
    private String description;

    @Column(name = "reference_id", nullable = false, updatable = false)
    private String referenceId;

    @Column(name="created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

}
