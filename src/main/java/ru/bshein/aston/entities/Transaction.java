package ru.bshein.aston.entities;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.bshein.aston.enums.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Account toAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime operationTime;

    public Transaction(Account fromAccount, Account toAccount, TransactionType type, BigDecimal amount, LocalDateTime operationTime) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.type = type;
        this.amount = amount;
        this.operationTime = operationTime;
    }
}
