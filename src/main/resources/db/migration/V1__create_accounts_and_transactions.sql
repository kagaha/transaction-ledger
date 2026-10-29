CREATE TABLE accounts (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
                          created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

                          CONSTRAINT pk_accounts PRIMARY KEY (id),
                          CONSTRAINT chk_account_balance_nonnegative
                              CHECK (balance >= 0)
);

CREATE TABLE transactions (
                              id BIGINT NOT NULL AUTO_INCREMENT,
                              account_id BIGINT NOT NULL,
                              type VARCHAR(10) NOT NULL,
                              amount DECIMAL(19, 2) NOT NULL,
                              description VARCHAR(255),
                              reference_id VARCHAR(100) NOT NULL,
                              balance_after_transaction DECIMAL(19, 2),
                              created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

                              CONSTRAINT pk_transactions PRIMARY KEY (id),
                              CONSTRAINT fk_transactions_account
                                  FOREIGN KEY (account_id)
                                      REFERENCES accounts(id),
                              CONSTRAINT uk_account_reference
                                  UNIQUE (account_id, reference_id),
                              CONSTRAINT chk_transaction_amount_positive
                                  CHECK (amount > 0),
                              CONSTRAINT chk_transaction_type
                                  CHECK (type IN ('CREDIT', 'DEBIT'))
);

CREATE INDEX idx_account_created_at
    ON transactions (account_id, created_at, id);