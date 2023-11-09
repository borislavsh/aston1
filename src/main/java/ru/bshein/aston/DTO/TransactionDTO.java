package ru.bshein.aston.DTO;

import lombok.Data;
import ru.bshein.aston.enums.TransactionType;
import ru.bshein.aston.entities.Transaction;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionDTO {


    private UUID fromAccount;
    private UUID toAccount;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime operationTime;

    public TransactionDTO(Transaction transaction) {

        this.fromAccount = transaction.getFromAccount().getId();
        this.toAccount = transaction.getToAccount() != null ? transaction.getToAccount().getId() : new UUID(0,0);
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.operationTime = transaction.getOperationTime();
    }
}
