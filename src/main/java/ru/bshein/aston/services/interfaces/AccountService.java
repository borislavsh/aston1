package ru.bshein.aston.services.interfaces;

import ru.bshein.aston.DTO.AccountDTO;
import ru.bshein.aston.entities.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    AccountDTO createAccount(String name, String pin);

    List<AccountDTO> findAllAccounts();

    Optional<Account> findAccount(UUID id);

    Account saveAccount(Account account);


}
