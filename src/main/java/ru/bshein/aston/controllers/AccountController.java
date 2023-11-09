package ru.bshein.aston.controllers;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bshein.aston.DTO.AccountCreationRequest;
import ru.bshein.aston.DTO.AccountDTO;

import ru.bshein.aston.services.interfaces.AccountService;


import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@Api(tags = "Account REST")
@Tag(name = "Account REST", description = "API for account management")
public class AccountController {

    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation(value = "Create account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Account successfully created"),
            @ApiResponse(code = 403, message = "Incorrect PIN code")
    })
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(
            @ApiParam(value = "Beneficiary name and PIN", required = true)
            @RequestBody AccountCreationRequest request) {
        AccountDTO accountDTO = accountService.createAccount(request.getBeneficiaryName(), request.getPin());
        return new ResponseEntity<>(accountDTO, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get all accounts", response = AccountDTO.class, responseContainer = "List")
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return new ResponseEntity<>(accountService.findAllAccounts(), HttpStatus.OK);
    }
}
