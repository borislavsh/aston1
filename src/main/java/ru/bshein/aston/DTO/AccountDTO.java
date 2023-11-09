package ru.bshein.aston.DTO;

import lombok.Data;
import ru.bshein.aston.entities.Account;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountDTO {

    private UUID id;
    private String beneficiaryName;
    private BigDecimal balance;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.beneficiaryName = account.getBeneficiaryName();
        this.balance = account.getBalance();
    }
}
