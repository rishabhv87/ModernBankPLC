package com.example.ModernBankPLC.services;

import com.example.ModernBankPLC.dto.AccountDTO;
import com.example.ModernBankPLC.dto.PaymentResponse;
import com.example.ModernBankPLC.exception.BusinessValidationException;
import com.example.ModernBankPLC.exception.InvalidAccoutNumberException;
import com.example.ModernBankPLC.model.PaymentRequest;
import com.example.ModernBankPLC.model.Transaction;
import com.example.ModernBankPLC.repository.IAccountsRepo;
import com.example.ModernBankPLC.repository.ITransactionRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentServicesTest {

    @Mock
    private IAccountsRepo accountRepo;
    @Mock
    private ITransactionRepo transactionRepo;
    @Mock
    private AccountServices accountService;


    @Test
    void makePaymentOnValidDebitAndValidCreditAccount() {
        PaymentServices paymentServices = new PaymentServices(accountRepo ,accountService ,transactionRepo );
        PaymentRequest paymentRequest = new PaymentRequest(111 , 222 , new BigDecimal(10.00));
        AccountDTO mockedDebitAccount = new AccountDTO(111, new BigDecimal(500.00) , "GBP", true);
        AccountDTO mockedCreditAccount = new AccountDTO(222, new BigDecimal(200.00) , "GBP", true);
        LinkedList<Transaction> mockedTransactionListResponseForDebitAccount = new LinkedList<>(List.of(
                new Transaction("txnId-1", 111, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now()),
                new Transaction("txnId-2", 111, 444 , new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now())
        ));
        LinkedList<Transaction> mockedTransactionListResponseForCreditAccount = new LinkedList<>(List.of(
                new Transaction("txnId-3", 222, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now())
        ));

        Mockito.when(accountService.getAccount(111)).thenReturn(mockedDebitAccount);
        Mockito.when(accountService.getAccount(222)).thenReturn(mockedCreditAccount);
        Mockito.when(transactionRepo.getTransactionListByAccountId(111)).thenReturn(Optional.of(mockedTransactionListResponseForDebitAccount));
        Mockito.when(transactionRepo.getTransactionListByAccountId(222)).thenReturn(Optional.of(mockedTransactionListResponseForCreditAccount));

        PaymentResponse paymentResponse = paymentServices.makePayment(paymentRequest);
        assertNotNull(paymentResponse.getTransactionId());



    }

    @Test
    void makePaymentFromInvalidDebitAccount() {
        PaymentServices paymentServices = new PaymentServices(accountRepo ,accountService ,transactionRepo );
        PaymentRequest paymentRequest = new PaymentRequest(111 , 222 , new BigDecimal(10.00));
        AccountDTO mockedCreditAccount = new AccountDTO(222, new BigDecimal(200.00) , "GBP", true);
        LinkedList<Transaction> mockedTransactionListResponseForDebitAccount = new LinkedList<>(List.of(
                new Transaction("txnId-1", 111, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now()),
                new Transaction("txnId-2", 111, 444 , new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now())
        ));
        LinkedList<Transaction> mockedTransactionListResponseForCreditAccount = new LinkedList<>(List.of(
                new Transaction("txnId-3", 222, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now())
        ));

        Mockito.when(accountService.getAccount(111)).thenReturn(null);
        Mockito.when(accountService.getAccount(222)).thenReturn(mockedCreditAccount);

        InvalidAccoutNumberException invalidAccoutNumberException = assertThrows(InvalidAccoutNumberException.class, () -> {
            paymentServices.makePayment(paymentRequest);
        },"Expected exception when fetching details with invalid accountId");
        assertEquals("Invalid Debit Account Number", invalidAccoutNumberException.getMessage());

    }



    @Test
    void makePaymentToInvalidCreditAccount() {
        PaymentServices paymentServices = new PaymentServices(accountRepo ,accountService ,transactionRepo );
        PaymentRequest paymentRequest = new PaymentRequest(111 , 222 , new BigDecimal(10.00));
        AccountDTO mockedDebitAccount = new AccountDTO(111, new BigDecimal(500.00) , "GBP", true);
        LinkedList<Transaction> mockedTransactionListResponseForDebitAccount = new LinkedList<>(List.of(
                new Transaction("txnId-1", 111, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now()),
                new Transaction("txnId-2", 111, 444 , new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now())
        ));
        LinkedList<Transaction> mockedTransactionListResponseForCreditAccount = new LinkedList<>(List.of(
                new Transaction("txnId-3", 222, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now())
        ));

        Mockito.when(accountService.getAccount(111)).thenReturn(mockedDebitAccount);
        Mockito.when(accountService.getAccount(222)).thenReturn(null);

        InvalidAccoutNumberException invalidAccoutNumberException = assertThrows(InvalidAccoutNumberException.class, () -> {
            paymentServices.makePayment(paymentRequest);
        },"Expected exception when fetching details with invalid accountId");
        assertEquals("Invalid Credit Account Number", invalidAccoutNumberException.getMessage());

    }


    @Test
    void makePaymentFromInactiveDebitToActiveCreditAccount() {
        PaymentServices paymentServices = new PaymentServices(accountRepo ,accountService ,transactionRepo );
        PaymentRequest paymentRequest = new PaymentRequest(111 , 222 , new BigDecimal(10.00));
        AccountDTO mockedDebitAccount = new AccountDTO(111, new BigDecimal(500.00) , "GBP", false);
        AccountDTO mockedCreditAccount = new AccountDTO(222, new BigDecimal(200.00) , "GBP", true);
        LinkedList<Transaction> mockedTransactionListResponseForDebitAccount = new LinkedList<>(List.of(
                new Transaction("txnId-1", 111, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now()),
                new Transaction("txnId-2", 111, 444 , new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now())
        ));
        LinkedList<Transaction> mockedTransactionListResponseForCreditAccount = new LinkedList<>(List.of(
                new Transaction("txnId-3", 222, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now())
        ));

        Mockito.when(accountService.getAccount(111)).thenReturn(mockedDebitAccount);
        Mockito.when(accountService.getAccount(222)).thenReturn(mockedCreditAccount);

        InvalidAccoutNumberException invalidAccoutNumberException = assertThrows(InvalidAccoutNumberException.class, () -> {
            paymentServices.makePayment(paymentRequest);
        },"Expected exception when fetching details with invalid accountId");
        assertEquals("Cannot make payment from inactive debit account", invalidAccoutNumberException.getMessage());

    }

    @Test
    void makePaymentFromValidDebitToInactiveCreditAccount() {
        PaymentServices paymentServices = new PaymentServices(accountRepo ,accountService ,transactionRepo );
        PaymentRequest paymentRequest = new PaymentRequest(111 , 222 , new BigDecimal(10.00));
        AccountDTO mockedDebitAccount = new AccountDTO(111, new BigDecimal(500.00) , "GBP", true);
        AccountDTO mockedCreditAccount = new AccountDTO(222, new BigDecimal(200.00) , "GBP", false);
        LinkedList<Transaction> mockedTransactionListResponseForDebitAccount = new LinkedList<>(List.of(
                new Transaction("txnId-1", 111, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now()),
                new Transaction("txnId-2", 111, 444 , new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now())
        ));
        LinkedList<Transaction> mockedTransactionListResponseForCreditAccount = new LinkedList<>(List.of(
                new Transaction("txnId-3", 222, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now())
        ));

        Mockito.when(accountService.getAccount(111)).thenReturn(mockedDebitAccount);
        Mockito.when(accountService.getAccount(222)).thenReturn(mockedCreditAccount);

        InvalidAccoutNumberException invalidAccoutNumberException = assertThrows(InvalidAccoutNumberException.class, () -> {
            paymentServices.makePayment(paymentRequest);
        },"Expected exception when fetching details with invalid accountId");
        assertEquals("Cannot make payment to inactive credit account", invalidAccoutNumberException.getMessage());

    }


    @Test
    void paymentFromValidDebitAccountWithInsuffientFund() {
        PaymentServices paymentServices = new PaymentServices(accountRepo ,accountService ,transactionRepo );
        PaymentRequest paymentRequest = new PaymentRequest(111 , 222 , new BigDecimal(1000.00));
        AccountDTO mockedDebitAccount = new AccountDTO(111, new BigDecimal(500.00) , "GBP", true);
        AccountDTO mockedCreditAccount = new AccountDTO(222, new BigDecimal(200.00) , "GBP", true);
        LinkedList<Transaction> mockedTransactionListResponseForDebitAccount = new LinkedList<>(List.of(
                new Transaction("txnId-1", 111, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now()),
                new Transaction("txnId-2", 111, 444 , new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now())
        ));
        LinkedList<Transaction> mockedTransactionListResponseForCreditAccount = new LinkedList<>(List.of(
                new Transaction("txnId-3", 222, 444 , new BigDecimal(10.00), "GBP", "DEBIT", LocalDateTime.now())
        ));

        Mockito.when(accountService.getAccount(111)).thenReturn(mockedDebitAccount);
        Mockito.when(accountService.getAccount(222)).thenReturn(mockedCreditAccount);
        Mockito.when(transactionRepo.getTransactionListByAccountId(111)).thenReturn(Optional.of(mockedTransactionListResponseForDebitAccount));
        Mockito.when(transactionRepo.getTransactionListByAccountId(222)).thenReturn(Optional.of(mockedTransactionListResponseForCreditAccount));

        BusinessValidationException insufficientFundException = assertThrows(BusinessValidationException.class, () -> {
            paymentServices.makePayment(paymentRequest);
        },"Expected exception when fetching details with invalid accountId");
        assertEquals("Debit account do not have sufficient fund", insufficientFundException.getMessage());

    }


    @Test
    void areAllBeansAutowired(){
        PaymentServices paymentServices = new PaymentServices(accountRepo ,accountService ,transactionRepo );
        assertNotNull(paymentServices.getAccountService());
        assertNotNull(paymentServices.getAccountsRepo());
        assertNotNull(paymentServices.getTransactionRepository());

    }

}