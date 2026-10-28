package com.example.transaction_ledger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name="accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(nullable = false, length=100)
    private String name;

    @Setter
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name="created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Account(String name) {
        this.name = name;
    }
}
