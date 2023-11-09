package ru.bshein.aston.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import ru.bshein.aston.DTO.TransactionDTO;
import ru.bshein.aston.DTO.TransactionRequest;
import ru.bshein.aston.DTO.TransferRequest;
import ru.bshein.aston.services.interfaces.TransactionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/transaction")
@Api(tags = "Transaction REST")
@Tag(name = "Transaction REST", description = "API for transaction management")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ApiOperation(value = "Get all transactions by account's number")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Wrong account number")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsByAccountId(@PathVariable UUID accountId) {
        return new ResponseEntity<>(transactionService.getAllTransactionsByAccountNumber(accountId),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Deposit to account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful deposit"),
            @ApiResponse(code = 403, message = "Incorrect PIN code"),
            @ApiResponse(code = 404, message = "Wrong account number")
    })
    @PostMapping("/deposit")
    public ResponseEntity<TransactionDTO> deposit(@RequestBody TransactionRequest transactionRequest) {
        return new ResponseEntity<>(transactionService.deposit(transactionRequest),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Withdraw from account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful withdraw"),
            @ApiResponse(code = 403, message = "Incorrect PIN code"),
            @ApiResponse(code = 404, message = "Wrong account number")
    })
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDTO> withdraw(@RequestBody TransactionRequest transactionRequest) {
        return new ResponseEntity<>(transactionService.withdraw(transactionRequest),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Transfer to account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful withdraw"),
            @ApiResponse(code = 400, message = "Insufficient funds"),
            @ApiResponse(code = 403, message = "Incorrect PIN code"),
            @ApiResponse(code = 404, message = "Wrong account number")
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> transfer(@RequestBody TransferRequest request) {
        return new ResponseEntity<>(transactionService.transfer(request),
                HttpStatus.OK);
    }


}
