package com.example.transaction_ledger.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ApiError(
            LocalDateTime timestamp,
            int status,
            String error,
            String message
    ) {}

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiError> handleAccountNotFound( AccountNotFoundException ex) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(DuplicateReferenceException.class)
    public ResponseEntity<ApiError> handleDuplicateReference( DuplicateReferenceException ex) {

        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiError> handleInsufficientFunds( InsufficientFundsException ex) {

        return buildResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage()
        );
    }

    @ExceptionHandler({InvalidTransactionException.class, IllegalArgumentException.class })
    public ResponseEntity<ApiError> handleBadRequest( RuntimeException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse("validation failed");
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String message;
        if ("type".equals(ex.getName())) {
            message = "Invalid transaction type. Allowed values are CREDIT and DEBIT.";
        } else {
            message = "Invalid value provided for parameter '" + ex.getName() + "'.";
        }

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        String message = "Invalid request body. Please check the supplied field values.";

        if (ex.getMessage() != null &&
                ex.getMessage().contains("TransactionType")) {
            message = "Invalid transaction type. Allowed values are CREDIT and DEBIT.";
        }

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException ex) {

        String message = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Invalid request parameters.");

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    private ResponseEntity<ApiError> buildResponse( HttpStatus status, String message) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return ResponseEntity.status(status).body(error);
    }
}