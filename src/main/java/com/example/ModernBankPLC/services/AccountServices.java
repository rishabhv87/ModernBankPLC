package com.example.ModernBankPLC.services;

import com.example.ModernBankPLC.dto.AccountDTO;
import com.example.ModernBankPLC.dto.BalanceDTO;
import com.example.ModernBankPLC.exception.ApiRequestException;
import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.dto.StatementDTO;
import com.example.ModernBankPLC.model.Transaction;
import com.example.ModernBankPLC.repository.IAccountsRepo;
import com.example.ModernBankPLC.repository.ITransactionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountServices {

    private final int NUMBER_OF_TRANSACTIONS_PER_STATEMENT = 20;

    @Autowired
    private IAccountsRepo accountsRepo;

    @Autowired
    private ITransactionRepo transactionRepo;


    public AccountDTO addAccount(AccountDTO accountDTO) {
        Account newAccount = new Account(accountDTO.getAccountId(), accountDTO.getBalance(), accountDTO.getCurrency(), true);
        Account insertedAccount= accountsRepo.addAccount(newAccount);
        return new AccountDTO(insertedAccount);
    }

    public List<AccountDTO> getAccountList() {
        List<Account> accountListFromDB = accountsRepo.getAccountList();
        return accountListFromDB.stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
    }

    public AccountDTO getAccount(int accountId) {
        Account accountFromDB = accountsRepo.getAccountByAccountId(accountId);
        if(accountFromDB == null){
            throw new ApiRequestException("Invalid Account Number");
        }
        return new AccountDTO(accountFromDB.getAccountId(),
                                accountFromDB.getBalance(),
                                accountFromDB.getCurrency(),
                                accountFromDB.isActive());
    }


    public BalanceDTO getBalance(int accountId) {
        Account accountWithBalance = accountsRepo.getAccountByAccountId(accountId);
        if(accountWithBalance == null){
            throw new ApiRequestException("Invalid Account Number");
        }
        return new BalanceDTO(accountWithBalance.getAccountId(), accountWithBalance.getBalance(), accountWithBalance.getCurrency());
    }



    public StatementDTO getMiniStatement(int accountId) {

        List<Transaction> transactionListForAccountId = transactionRepo.getTransactionListByAccountId(accountId);
        if (transactionListForAccountId == null) {
            throw new ApiRequestException("Invalid Account Number");
        }

        List<Transaction> transactionList = transactionListForAccountId.size() >= 20
                ? transactionListForAccountId.subList(0, NUMBER_OF_TRANSACTIONS_PER_STATEMENT)
                : transactionListForAccountId;

        return new StatementDTO(transactionList);
    }

    public AccountDTO deleteAccount(int accountId) {
        Account accountFromDB = accountsRepo.deleteAccountByAccountId(accountId);
        if(accountFromDB == null){
            throw new ApiRequestException("Invalid Account Number");
        }

        return new AccountDTO(accountFromDB.getAccountId(),
                accountFromDB.getBalance(),
                accountFromDB.getCurrency(),
                accountFromDB.isActive());
    }

}
