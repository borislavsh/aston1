package ru.bshein.aston.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    private UUID fromAccount;
    private UUID toAccount;
    private String pin;
    private BigDecimal amount;
}
