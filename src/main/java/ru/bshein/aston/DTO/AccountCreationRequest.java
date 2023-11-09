package ru.bshein.aston.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountCreationRequest {
    private String beneficiaryName;
    private String pin;

}
