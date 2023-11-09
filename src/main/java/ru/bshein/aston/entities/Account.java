package ru.bshein.aston.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String beneficiaryName;

    private String pinCode;

    @EqualsAndHashCode.Exclude
    private BigDecimal balance;

    public Account(String beneficiaryName, String pinCode) {
        this.beneficiaryName = beneficiaryName;
        this.pinCode = pinCode;
        this.balance = BigDecimal.ZERO;
    }


}
