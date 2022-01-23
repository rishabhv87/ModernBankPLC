package com.example.ModernBankPLC.repository;

import com.example.ModernBankPLC.model.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountsRepo {

    Account addAccount(Account account);

    Optional<Account> getAccountByAccountId(int accountId);

    Account deleteAccountByAccountId(int accountId);

    List<Account> getAccountList();

    Account updateAccountDetails(Account account);



}
