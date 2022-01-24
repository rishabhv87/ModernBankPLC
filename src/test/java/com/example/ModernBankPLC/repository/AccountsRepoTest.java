package com.example.ModernBankPLC.repository;

import com.example.ModernBankPLC.constants.ErrorConstants;
import com.example.ModernBankPLC.exception.BusinessValidationException;
import com.example.ModernBankPLC.exception.InvalidAccoutNumberException;
import com.example.ModernBankPLC.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
class AccountsRepoTest {

    private static Map<Integer , Account> mockedAccountDB= new HashMap<>();

    static {
        mockedAccountDB.put(111, new Account(111, new BigDecimal(500.00) , "GBP", true));
        mockedAccountDB.put(222, new Account(222, new BigDecimal(500.00) , "GBP",true));
        mockedAccountDB.put(333, new Account(333, new BigDecimal(500.00) , "GBP", true));
        mockedAccountDB.put(444, new Account(444, new BigDecimal(500.00) , "GBP",true));
        mockedAccountDB.put(555, new Account(555, new BigDecimal(500.00) , "GBP", true));
    }
    @Mock
    private static Map<Integer , Account> accountsDB ;

    @Mock
    private InternalInMemoryDbClass internalInMemoryDB;








    @Test
    void getAccountList() {
        IAccountsRepo accountsRepo = new AccountsRepo(internalInMemoryDB);
        Mockito.when(internalInMemoryDB.getAccountDB()).thenReturn(mockedAccountDB);
        List<Account> accountList = accountsRepo.getAccountList();
        assertEquals(5, accountList.size());
    }

    @Test
    void addAccount() {
 Account newAccount = new Account(666, new BigDecimal(550.00), "GBP", true);
        IAccountsRepo accountsRepo = new AccountsRepo(internalInMemoryDB);
        Mockito.when(internalInMemoryDB.getAccountDB()).thenReturn(mockedAccountDB);

        Account addedAccount = accountsRepo.addAccount(newAccount);
        assertEquals(666, addedAccount.getAccountId());
        assertEquals(new BigDecimal(550), addedAccount.getBalance());
        assertEquals("GBP", addedAccount.getCurrency());
        assertEquals(6, accountsRepo.getAccountList().size());
    }

    @Test
    void shouldThrowErrorIfAddExistingAccount() {
        Account newAccount = new Account(444, new BigDecimal(550.00), "GBP", true);
        IAccountsRepo accountsRepo = new AccountsRepo(internalInMemoryDB);
        Mockito.when(internalInMemoryDB.getAccountDB()).thenReturn(mockedAccountDB);
        BusinessValidationException businessValidationException = assertThrows(BusinessValidationException.class, () -> {
            accountsRepo.addAccount(newAccount);
        },"Expected exception when fetching details with invalid accountId");
        assertEquals(ErrorConstants.ACCOUNT_ALREADY_EXISTS, businessValidationException.getMessage());
    }

    @Test
    void updateAccountDetails() {
        Account accountToBeUpdated = new Account(555, new BigDecimal(700.00), "GBP", true);
        IAccountsRepo accountsRepo = new AccountsRepo(internalInMemoryDB);
        Mockito.when(internalInMemoryDB.getAccountDB()).thenReturn(mockedAccountDB);

        Account updatedAccount = accountsRepo.updateAccountDetails(accountToBeUpdated);
        assertEquals(555, updatedAccount.getAccountId());
        assertEquals(new BigDecimal(700.00), updatedAccount.getBalance());
        assertEquals("GBP", updatedAccount.getCurrency());
    }


    @Test
    void shouldDeleteAccountByAccountId() {

        IAccountsRepo accountsRepo = new AccountsRepo(internalInMemoryDB);
        Mockito.when(internalInMemoryDB.getAccountDB()).thenReturn(mockedAccountDB);

        Account deletedAccount = accountsRepo.deleteAccountByAccountId(555);
        assertEquals(555, deletedAccount.getAccountId());
        assertEquals(new BigDecimal(500.00), deletedAccount.getBalance());
        assertEquals("GBP", deletedAccount.getCurrency());
        assertEquals(false, deletedAccount.isActive());
    }

    @Test
    void shouldReturnNullWhenDeleteAccountThatDoesNotExist() {
        IAccountsRepo accountsRepo = new AccountsRepo(internalInMemoryDB);
        Mockito.when(internalInMemoryDB.getAccountDB()).thenReturn(mockedAccountDB);
        InvalidAccoutNumberException invalidAccoutNumberException = assertThrows(InvalidAccoutNumberException.class, () -> {
            accountsRepo.deleteAccountByAccountId(666);
        },"Expected exception when fetching details with invalid accountId");
        assertEquals(ErrorConstants.INVALID_ACCOUNT_NUMBER, invalidAccoutNumberException.getMessage());


    }
}