package com.example.ModernBankPLC.repository;


import com.example.ModernBankPLC.model.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class AccountsRepo implements IAccountsRepo {

    @Autowired
    InternalInMemoryDbClass internalInMemoryDB;

    static Map<Integer, Account> internalAccountsDB;

   /* public static Map<Integer, Account> accountsDB = new HashMap<>();

    static {
        accountsDB.put(111, new Account(111, new BigDecimal(500.00), "GBP", true));
        accountsDB.put(222, new Account(222, new BigDecimal(500.00), "GBP", true));
        accountsDB.put(333, new Account(333, new BigDecimal(500.00), "GBP", true));
        accountsDB.put(444, new Account(444, new BigDecimal(500.00), "GBP", true));
        accountsDB.put(555, new Account(555, new BigDecimal(500.00), "GBP", true));
    }*/

    public AccountsRepo(InternalInMemoryDbClass internalInMemoryDB) {
        this.internalInMemoryDB = internalInMemoryDB;
    }

    @Override
    public Optional<Account> getAccountByAccountId(int accountId) {
        return Optional.ofNullable(accountsDB().get(accountId));
    }

    @Override
    public Account deleteAccountByAccountId(int accountId) {
        internalAccountsDB = accountsDB();
        if (!internalAccountsDB.containsKey(accountId)) {
            return null;
        }
        internalAccountsDB.get(accountId).setActive(false);  // Delete the account , in a way inactivate the account.
        return internalAccountsDB.get(accountId);
    }

    @Override
    public List<Account> getAccountList() {

        return accountsDB().values().stream().collect(Collectors.toList());
    }

    @Override
    public Account addAccount(Account account) {
        accountsDB().put(account.getAccountId(), account);
        return account;
    }

    private Map<Integer, Account> accountsDB() {
        return internalInMemoryDB.getAccountDB();
    }

    @Override
    public Account updateAccountDetails(Account account) {
        accountsDB().put(account.getAccountId(), account);
        return account;
    }







    /*
    @Autowired
    InternalInMemoryDbClass internalInMemoryDB;

    public static Map<Integer, Account> accountsDB = new HashMap<>();

    static {
        accountsDB.put(111, new Account(111, new BigDecimal(500.00), "GBP", true));
        accountsDB.put(222, new Account(222, new BigDecimal(500.00), "GBP", true));
        accountsDB.put(333, new Account(333, new BigDecimal(500.00), "GBP", true));
        accountsDB.put(444, new Account(444, new BigDecimal(500.00), "GBP", true));
        accountsDB.put(555, new Account(555, new BigDecimal(500.00), "GBP", true));
    }


    @Override
    public Optional<Account> getAccountByAccountId(int accountId) {

        return Optional.ofNullable(accountsDB.get(accountId));
    }

    @Override
    public Account deleteAccountByAccountId(int accountId) {
        if (!accountsDB.containsKey(accountId)) {
            return null;
        }
        accountsDB.get(accountId).setActive(false);  // Delete the account , in a way inactivate the account.
        return accountsDB.get(accountId);
    }

    @Override
    public List<Account> getAccountList() {

        Map<Integer, Account> internalAccountsDB = internalInMemoryDB.getInternalAccountsDB();
        return internalAccountsDB.values().stream().collect(Collectors.toList());
        //return accountsDB.values().stream().collect(Collectors.toList());
    }

    @Override
    public Account addAccount(Account account) {
        accountsDB.put(account.getAccountId(), account);
        return account;
    }

    @Override
    public Account updateAccountDetails(Account account) {
        accountsDB.put(account.getAccountId(), account);
        return account;
    }

     */


}