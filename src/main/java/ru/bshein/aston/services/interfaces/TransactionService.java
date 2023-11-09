package ru.bshein.aston.services.interfaces;

import ru.bshein.aston.DTO.TransactionDTO;
import ru.bshein.aston.DTO.TransactionRequest;
import ru.bshein.aston.DTO.TransferRequest;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

        TransactionDTO deposit(TransactionRequest request);

        TransactionDTO withdraw(TransactionRequest request);

        TransactionDTO transfer(TransferRequest request);

        List<TransactionDTO> getAllTransactionsByAccountNumber(UUID number);
}
