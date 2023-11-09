package ru.bshein.aston.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bshein.aston.DTO.TransactionDTO;
import ru.bshein.aston.DTO.TransactionRequest;
import ru.bshein.aston.DTO.TransferRequest;
import ru.bshein.aston.enums.TransactionType;
import ru.bshein.aston.exceptions.AccountNotFoundException;
import ru.bshein.aston.exceptions.InsufficientFundsException;
import ru.bshein.aston.exceptions.InvalidPinException;
import ru.bshein.aston.entities.Account;
import ru.bshein.aston.entities.Transaction;
import ru.bshein.aston.repositories.TransactionRepository;
import ru.bshein.aston.services.interfaces.AccountService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionServiceImpl transactionService;
    private TransferRequest transferRequest;
    private TransactionRequest transactionRequest;
    private Account account;
    private Account toAccount;


    @BeforeEach
    public void setUp() {
        UUID accountId = UUID.randomUUID();
        UUID toAccountId = UUID.randomUUID();

        account = new Account();
        account.setId(accountId);
        account.setBalance(BigDecimal.valueOf(5000));
        account.setPinCode("1234");
        account.setBeneficiaryName("John Doe");

        toAccount = new Account();
        toAccount.setId(toAccountId);
        toAccount.setBalance(BigDecimal.valueOf(0));
        toAccount.setPinCode("1234");
        toAccount.setBeneficiaryName("John Snow");

        transactionRequest = new TransactionRequest();
        transactionRequest.setPin("1234");
        transactionRequest.setAmount(BigDecimal.valueOf(100));
        transactionRequest.setAccountId(accountId);

        transferRequest = new TransferRequest();
        transferRequest.setPin("1234");
        transferRequest.setAmount(BigDecimal.valueOf(50));
        transferRequest.setFromAccount(accountId);
        transferRequest.setToAccount(toAccountId);

    }

    @Test
    public void testDepositWhenValidTransactionThenSuccess() {
        when(accountService.findAccount(transactionRequest.getAccountId())).thenReturn(Optional.of(account));
        when(accountService.saveAccount(account)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        TransactionDTO result = transactionService.deposit(transactionRequest);

        assertEquals(transactionRequest.getAccountId(), result.getFromAccount());
        assertEquals(TransactionType.DEPOSIT, result.getType());
        assertEquals(transactionRequest.getAmount(), result.getAmount());
    }

    @Test
    public void testDepositWhenAccountNotFoundThenThrowException() {
        when(accountService.findAccount(transactionRequest.getAccountId())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transactionService.deposit(transactionRequest));
    }

    @Test
    public void testDepositWhenIncorrectPinThenThrowException() {
        transactionRequest.setPin("wrong pin");
        when(accountService.findAccount(transactionRequest.getAccountId())).thenReturn(Optional.of(account));

        assertThrows(InvalidPinException.class, () -> transactionService.deposit(transactionRequest));
    }

    @Test
    public void testWithdrawWhenInsufficientBalanceThenThrowException() {
        transactionRequest.setAmount(BigDecimal.valueOf(6000));
        when(accountService.findAccount(transactionRequest.getAccountId())).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> transactionService.withdraw(transactionRequest));
    }

    @Test
    public void testTransferWhenValidTransactionThenSuccess() {
        when(accountService.findAccount(transferRequest.getFromAccount())).thenReturn(Optional.of(account));
        when(accountService.findAccount(transferRequest.getToAccount())).thenReturn(Optional.of(toAccount));
        when(accountService.saveAccount(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        TransactionDTO result = transactionService.transfer(transferRequest);

        assertEquals(transferRequest.getFromAccount(), result.getFromAccount());
        assertEquals(transferRequest.getToAccount(), result.getToAccount());
        assertEquals(TransactionType.TRANSFER, result.getType());
        assertEquals(transferRequest.getAmount(), result.getAmount());
    }

    @Test
    public void testTransferWhenSenderAccountNotFoundThenThrowException() {
        when(accountService.findAccount(transferRequest.getFromAccount())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transactionService.transfer(transferRequest));
    }

    @Test
    public void testTransferWhenReceiverAccountNotFoundThenThrowException() {
        when(accountService.findAccount(transferRequest.getFromAccount())).thenReturn(Optional.of(account));
        when(accountService.findAccount(transferRequest.getToAccount())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transactionService.transfer(transferRequest));
    }

    @Test
    public void testTransferWhenIncorrectPinThenThrowException() {
        transferRequest.setPin("wrong pin");
        when(accountService.findAccount(any(UUID.class))).thenReturn(Optional.of(account));

        assertThrows(InvalidPinException.class, () -> transactionService.transfer(transferRequest));
    }

    @Test
    public void testTransferWhenInsufficientBalanceThenThrowException() {
        transferRequest.setAmount(BigDecimal.valueOf(6000));
        when(accountService.findAccount(any(UUID.class))).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> transactionService.transfer(transferRequest));
    }

    @Test
    public void testGetAllTransactionsByAccountNumberWhenValidAccountNumberThenSuccess() {
        Transaction transaction = new Transaction(account, null, TransactionType.DEPOSIT, BigDecimal.valueOf(10), LocalDateTime.now());
        when(accountService.findAccount(account.getId())).thenReturn(Optional.of(account));
        when(transactionRepository.findAllByFromAccountOrToAccount(account, account)).thenReturn(Collections.singletonList(transaction));

        List<TransactionDTO> result = transactionService.getAllTransactionsByAccountNumber(account.getId());

        assertEquals(1, result.size());
        assertEquals(account.getId(), result.get(0).getFromAccount());
        assertEquals(TransactionType.DEPOSIT, result.get(0).getType());
        assertEquals(BigDecimal.valueOf(10), result.get(0).getAmount());
    }

    @Test
    public void testGetAllTransactionsByAccountNumberWhenAccountNotFoundThenThrowException() {
        when(accountService.findAccount(account.getId())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.getAllTransactionsByAccountNumber(account.getId()));
    }
}