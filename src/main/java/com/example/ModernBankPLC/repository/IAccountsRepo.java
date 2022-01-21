package com.example.ModernBankPLC.repository;

import com.example.ModernBankPLC.model.Account;

import java.util.List;

public interface IAccountsRepo {

    Account addAccount(Account account);

    Account getAccountByAccountId(int accountId);

    Account deleteAccountByAccountId(int accountId);

    List<Account> getAccountList();

    Account updateAccountDetails(Account account);



}
