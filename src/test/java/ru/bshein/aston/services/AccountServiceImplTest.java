package ru.bshein.aston.services;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bshein.aston.DTO.AccountDTO;
import ru.bshein.aston.exceptions.InvalidPinException;
import ru.bshein.aston.entities.Account;
import ru.bshein.aston.repositories.AccountRepository;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;



    @Test
    public void testCreateAccountWithValidInputs() {
        Account account = new Account("John Doe", "1234");
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDTO result = accountService.createAccount("John Doe", "1234");

        assertEquals(account.getBeneficiaryName(), result.getBeneficiaryName());
        assertEquals(account.getPinCode(), result.getPin());
    }

    @Test
    public void testCreateAccountWithInvalidPin() {
        assertThrows(InvalidPinException.class, () -> accountService.createAccount("John Doe", "123"));
    }

    @Test
    public void testFindAllAccounts() {
        Account account1 = new Account("John Doe", "1234");
        Account account2 = new Account("Jane Doe", "5678");
        when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

        List<AccountDTO> result = accountService.findAllAccounts();

        assertEquals(2, result.size());
        assertEquals(account1.getBeneficiaryName(), result.get(0).getBeneficiaryName());
        assertEquals(account2.getBeneficiaryName(), result.get(1).getBeneficiaryName());
    }

    @Test
    public void testFindAccountWithExistingId() {
        UUID id = UUID.randomUUID();
        Account account = new Account("John Doe", "1234");
        account.setId(id);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.findAccount(id);

        assertTrue(result.isPresent());
        assertEquals(account, result.get());
    }

    @Test
    public void testFindAccountWithNonExistingId() {
        UUID id = UUID.randomUUID();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Account> result = accountService.findAccount(id);

        assertFalse(result.isPresent());
    }

    @Test
    public void testSaveAccount() {
        Account account = new Account("John Doe", "1234");
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.saveAccount(account);

        assertEquals(account, result);
    }
}