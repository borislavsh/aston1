package ru.bshein.aston.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.bshein.aston.services.interfaces.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    @Transactional
    public TransactionDTO deposit(TransactionRequest request) {
        Account account = getAccount(request.getAccountId(),
                "Invalid beneficiary account number");
        checkPinCode(request.getPin(), account);

        account.setBalance(account.getBalance().add(request.getAmount()));
        Account accountWithDeposit = accountService.saveAccount(account);

        Transaction transaction = new Transaction(accountWithDeposit,
                null,
                TransactionType.DEPOSIT,
                request.getAmount(),
                LocalDateTime.now());

        return new TransactionDTO(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public TransactionDTO withdraw(TransactionRequest request) {
        Account account = getAccount(request.getAccountId(),
                "Invalid beneficiary account number");
        checkPinCode(request.getPin(), account);
        checkBalance(request.getAmount(), account);

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        Account updatedAccount = accountService.saveAccount(account);

        Transaction transaction = new Transaction(updatedAccount,
                null,
                TransactionType.WITHDRAW,
                request.getAmount(),
                LocalDateTime.now());

        return new TransactionDTO(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public TransactionDTO transfer(TransferRequest request) {
        Account accountSender = getAccount(request.getFromAccount(),
                "Invalid sender account number");
        Account accountReceiver = getAccount(request.getToAccount(),
                "Invalid sender account number");
        checkPinCode(request.getPin(), accountSender);
        checkBalance(request.getAmount(), accountSender);

        accountSender.setBalance(accountSender.getBalance().subtract(request.getAmount()));
        accountReceiver.setBalance(accountReceiver.getBalance().add(request.getAmount()));

        Account updAccountSender = accountService.saveAccount(accountSender);
        Account updAccountReceiver = accountService.saveAccount(accountReceiver);

        Transaction transaction = new Transaction(updAccountSender,
                updAccountReceiver,
                TransactionType.TRANSFER,
                request.getAmount(),
                LocalDateTime.now());

        return new TransactionDTO(transactionRepository.save(transaction));
    }

    @Override
    public List<TransactionDTO> getAllTransactionsByAccountNumber(UUID number) {
        Account account = getAccount(number, "Invalid beneficiary account number");
        List<Transaction> transactions = transactionRepository.findAllByFromAccountOrToAccount(account, account);

        return transactions.stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }


    private Account getAccount(UUID accountId, String message) {
        return accountService.findAccount(accountId)
                .orElseThrow(() -> new AccountNotFoundException(message));
    }

    private void checkPinCode(String pin, Account account) {
        if(!pin.equals(account.getPinCode())) {
            throw new InvalidPinException("Wrong PIN");
        }
    }

    private void checkBalance(BigDecimal amount, Account account) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("There are insufficient funds in your account");

        }
    }

}
