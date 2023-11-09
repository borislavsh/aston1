package ru.bshein.aston.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bshein.aston.DTO.AccountDTO;
import ru.bshein.aston.exceptions.InvalidPinException;
import ru.bshein.aston.entities.Account;
import ru.bshein.aston.repositories.AccountRepository;
import ru.bshein.aston.services.interfaces.AccountService;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public AccountDTO createAccount(String name, String pin) {
        if (!pin.matches("\\d{4}")) {
            throw new InvalidPinException("PIN should be 4 digits");
        }
        Account account = new Account(name, pin);
        return new AccountDTO(accountRepository.save(account));
    }

    @Override
    public List<AccountDTO> findAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Account> findAccount(UUID id) {
        accountRepository.flush();
        return accountRepository.findById(id);
    }

    @Override
    @Transactional
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

}

