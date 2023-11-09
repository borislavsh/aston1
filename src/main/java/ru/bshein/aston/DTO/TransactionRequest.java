package ru.bshein.aston.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bshein.aston.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    UUID accountId;
    String pin;
    BigDecimal amount;
    TransactionType type;

}
