package com.example.transaction_ledger.controller;


import com.example.transaction_ledger.dto.request.TransferRequest;
import com.example.transaction_ledger.dto.response.TransferResponse;
import com.example.transaction_ledger.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfer")
public class TransferController {


    private final TransferService transferService;

    public TransferController(TransferService transferService){
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> transferAmount(@Valid @RequestBody TransferRequest request){

        TransferResponse response = transferService.transferAmount(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}
