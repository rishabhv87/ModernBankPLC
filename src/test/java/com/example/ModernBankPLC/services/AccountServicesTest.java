package com.example.ModernBankPLC.services;

import com.example.ModernBankPLC.dto.AccountDTO;
import com.example.ModernBankPLC.dto.BalanceDTO;
import com.example.ModernBankPLC.dto.StatementDTO;
import com.example.ModernBankPLC.exception.InvalidAccoutNumberException;
import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.model.Transaction;
import com.example.ModernBankPLC.repository.IAccountsRepo;
import com.example.ModernBankPLC.repository.ITransactionRepo;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
class AccountServicesTest {


    @Mock
    private IAccountsRepo accountRepo;
    @Mock
    private ITransactionRepo transactionRepo ;

    @Test
    void shouldGetAccountList() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        List<Account> mockedAccountListResult = new ArrayList<>
                (List.of
                        (new Account(111, new BigDecimal(500.00) , "GBP", true),
                         new Account(222, new BigDecimal(250.00) , "GBP", false)
        ));
        Mockito.when(accountRepo.getAccountList()).thenReturn(mockedAccountListResult);
        List<AccountDTO> accountDTOList = accountServices.getAccountList();
        assertNotNull(accountDTOList);
        assertEquals(2, accountDTOList.size());
        assertEquals(111 , accountDTOList.get(0).getAccountId());
        assertEquals(false , accountDTOList.get(1).isActive());
    }

    @Test
    void shouldGetAccountDTOWhenAccountIsPresentInRepo() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        Account mockedAccountResult = new Account(333, new BigDecimal(500.00) , "GBP", true);
        Mockito.when(accountRepo.getAccountByAccountId(333)).thenReturn(Optional.of(mockedAccountResult));
        AccountDTO returnedAccountDTO = accountServices.getAccount(333);
        assertEquals(333, returnedAccountDTO.getAccountId());
    }

    @Test
    void shouldGetAccountThrowExceptionWhenAccountIsPresentInRepo() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        Mockito.when(accountRepo.getAccountByAccountId(333)).thenReturn(Optional.ofNullable(null));
        InvalidAccoutNumberException invalidAccoutNumberException = assertThrows(InvalidAccoutNumberException.class, () -> {
                    accountServices.getAccount(333);
                },
                "Expected exception when fetching details with invalid accountId");

        //assertEquals(HttpStatus.BAD_REQUEST , invalidAccoutNumberException.);
        assertEquals("Invalid Account Number", invalidAccoutNumberException.getMessage());
    }

    @Test
    void shouldGetBalanceThrowExceptionWhenAccountIsPresentInRepo() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        Mockito.when(accountRepo.getAccountByAccountId(333)).thenReturn(Optional.ofNullable(null));
        InvalidAccoutNumberException invalidAccoutNumberException = assertThrows(InvalidAccoutNumberException.class, () -> {
                    accountServices.getBalance(333);
                },
                "Expected exception when fetching details with invalid accountId");

        //assertEquals(HttpStatus.BAD_REQUEST , invalidAccoutNumberException.);
        assertEquals("Invalid Account Number", invalidAccoutNumberException.getMessage());
    }




    @Test
    void shouldGetBalanceFromValidAccount() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        Account mockedAccountResult = new Account(333, new BigDecimal(500.00) , "GBP", true);
        Mockito.when(accountRepo.getAccountByAccountId(333)).thenReturn(Optional.of(mockedAccountResult));
        BalanceDTO returnedBalanceDTO = accountServices.getBalance(333);
        assertEquals(333, returnedBalanceDTO.getAccountId());
        assertEquals(new BigDecimal(500), returnedBalanceDTO.getBalance());
    }

    @Test
    void shouldThrowErrorWhenGetBalanceForInvalidAccount() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        Mockito.when(accountRepo.getAccountByAccountId(333)).thenReturn(Optional.ofNullable(null));
        InvalidAccoutNumberException invalidAccoutNumberException = assertThrows(InvalidAccoutNumberException.class, () -> {
            accountServices.getBalance(333);
        },"Expected exception when fetching details with invalid accountId");
        assertEquals("Invalid Account Number", invalidAccoutNumberException.getMessage());
    }

    @Test
    void shouldGetMiniStatementIfAccountIdIsPresentInTrasactionRepo() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);

        LinkedList<Transaction> mockedTransactionListResponse = new LinkedList<>(List.of(
                new Transaction("txnId-1", 333, 444, new BigDecimal(10), "GBP", "DEBIT", LocalDateTime.now()),
                new Transaction("txnId-2", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now())

        ));

        Mockito.when(transactionRepo.getTransactionListByAccountId(333)).thenReturn(Optional.of(mockedTransactionListResponse));
        StatementDTO miniStatement = accountServices.getMiniStatement(333);

        assertEquals(2, miniStatement.getTransactionList().size());
        assertEquals(333 , miniStatement.getTransactionList().get(0).getAccountNumber());

    }


    @Test
    void shouldGetMiniStatementOfMax20transactions() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);

        LinkedList<Transaction> mockedTransactionListResponse = new LinkedList<>(List.of(
                new Transaction("txnId-1", 333, 444, new BigDecimal(10), "GBP", "DEBIT", LocalDateTime.now()),
                new Transaction("txnId-2", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-3", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-4", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-5", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-6", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-7", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-8", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-9", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-10", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-11", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-12", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-13", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-14", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-15", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-16", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-17", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-18", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-19", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-20", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-21", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-22", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-23", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-24", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-25", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-26", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-27", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-28", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-29", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now()),
                new Transaction("txnId-30", 333, 111, new BigDecimal(5.00), "GBP", "CREDIT", LocalDateTime.now())
        ));

        Mockito.when(transactionRepo.getTransactionListByAccountId(333)).thenReturn(Optional.of(mockedTransactionListResponse));
        StatementDTO miniStatement = accountServices.getMiniStatement(333);
        assertEquals(20, miniStatement.getTransactionList().size());
    }



    @Test
    void shouldThrowExceptionIfAccountIdIsNotPresentInTrasactionRepo() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        Mockito.when(transactionRepo.getTransactionListByAccountId(333)).thenReturn(Optional.ofNullable(null));
        InvalidAccoutNumberException invalidAccoutNumberException = assertThrows(InvalidAccoutNumberException.class, () -> {
                    accountServices.getMiniStatement(333);
                },"Expected exception when fetching details with invalid accountId");
        assertEquals("Invalid Account Number", invalidAccoutNumberException.getMessage());

    }




    @Test
    void shouldDeleteAccount() {

        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        Account mockedAccountResult = new Account(333, new BigDecimal(500.00) , "GBP", false);
        Mockito.when(accountRepo.deleteAccountByAccountId(333)).thenReturn(mockedAccountResult);
        AccountDTO returnedAccountDTO = accountServices.deleteAccount(333);
        assertEquals(false , returnedAccountDTO.isActive());
    }

    @Test
    void shouldThrowExceptionWhenDeleteInvalidAccount() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        InvalidAccoutNumberException invalidAccoutNumberException = assertThrows(InvalidAccoutNumberException.class, () -> {
            accountServices.deleteAccount(333);
        },"Expected exception when fetching details with invalid accountId");
        assertEquals("Invalid Account Number", invalidAccoutNumberException.getMessage());
    }


    @Test
    void updateAccount() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        AccountDTO accountDTO = new AccountDTO(333, new BigDecimal(500.00) , "GBP");
        Account account = new Account(accountDTO.getAccountId(), accountDTO.getBalance(), accountDTO.getCurrency(), true);
        Account mockedAccountResult = new Account(333, new BigDecimal(500.00) , "GBP", true);
        Mockito.when(accountRepo.getAccountByAccountId(333)).thenReturn(Optional.ofNullable(account));
        AccountDTO returnedAccountDTO = accountServices.updateAccountInRepository(accountDTO);
        assertEquals(333, returnedAccountDTO.getAccountId());
    }
    @Test
    void shouldThrowErrorWhenUpdateInvalidAccount() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        AccountDTO accountDTO = new AccountDTO(333, new BigDecimal(500.00) , "GBP");
        Mockito.when(accountRepo.getAccountByAccountId(333)).thenReturn(Optional.ofNullable(null));
        InvalidAccoutNumberException invalidAccoutNumberException = assertThrows(InvalidAccoutNumberException.class, () -> {
            accountServices.updateAccountInRepository(accountDTO);
        },"Expected exception when fetching details with invalid accountId");
        assertEquals("Invalid Account Number", invalidAccoutNumberException.getMessage());
    }


    @Test
    void addAccount() {
        AccountServices accountServices = new AccountServices(accountRepo , transactionRepo);
        AccountDTO accountDTO = new AccountDTO(333, new BigDecimal(500.00) , "GBP");
        Account account = new Account(accountDTO.getAccountId(), accountDTO.getBalance(), accountDTO.getCurrency(), true);
        Account mockedAccountResult = new Account(333, new BigDecimal(500.00) , "GBP", true);
        Mockito.when(accountRepo.addAccount(account)).thenReturn(mockedAccountResult);
        AccountDTO returnedAccountDTO = accountServices.addAccount(accountDTO);
        assertEquals(333, returnedAccountDTO.getAccountId());
    }


    @Test
    void areAllBeansAutowired(){
        AccountServices accountServices = new AccountServices(accountRepo  ,transactionRepo );
        assertNotNull(accountServices.getAccountsRepo());
        assertNotNull(accountServices.getTransactionRepo());

    }
}