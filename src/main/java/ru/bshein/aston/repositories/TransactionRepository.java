package ru.bshein.aston.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bshein.aston.entities.Account;
import ru.bshein.aston.entities.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByFromAccountOrToAccount(Account fromAccount, Account toAccount);

}
