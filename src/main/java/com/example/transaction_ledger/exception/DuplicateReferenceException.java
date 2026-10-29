package com.example.transaction_ledger.exception;

public class DuplicateReferenceException extends RuntimeException {
    public DuplicateReferenceException(String referenceId) {
        super("Reference ID already exists: " + referenceId);
    }
}
