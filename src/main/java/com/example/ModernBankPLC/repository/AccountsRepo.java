package com.example.ModernBankPLC.repository;


import com.example.ModernBankPLC.model.Account;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class AccountsRepo implements IAccountsRepo{

    private static Map<Integer , Account> accountsDB= new HashMap<>();

    static {
        accountsDB.put(111, new Account(111, 500.00 , "GBP", true));
        accountsDB.put(222, new Account(222, 500.00 , "GBP",true));
        accountsDB.put(333, new Account(333, 500.00 , "GBP", true));
        accountsDB.put(444, new Account(444, 500.00 , "GBP",true));
        accountsDB.put(555, new Account(555, 500.00 , "GBP", true));
    }



    @Override
    public Account getAccountByAccountId(int accountId) {

        return accountsDB.get(accountId);
    }

    @Override
    public Account deleteAccountByAccountId(int accountId) {
        if(!accountsDB.containsKey(accountId)){
            return null;
        }
        accountsDB.get(accountId).setActive(false);  // Delete the account , in a way inactivate the account.
        return accountsDB.get(accountId);
    }

    @Override
    public List<Account> getAccountList() {
        return accountsDB.values().stream().collect(Collectors.toList());
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
}
