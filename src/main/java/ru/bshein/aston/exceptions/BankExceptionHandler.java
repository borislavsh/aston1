package ru.bshein.aston.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@RestControllerAdvice
public class BankExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handlerAccountNotFoundException(AccountNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handlerException(Exception message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message.getMessage());
    }

    @ExceptionHandler(value = {InsufficientFundsException.class})
    public ResponseEntity<Object> handleTransactionExceptions(RuntimeException message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message.getMessage());
    }

    @ExceptionHandler(InvalidPinException.class)
    public ResponseEntity<Object> handlePinExceptions(RuntimeException message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message.getMessage());
    }

}
