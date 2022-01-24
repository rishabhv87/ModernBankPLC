package com.example.ModernBankPLC.repository;


import com.example.ModernBankPLC.constants.ErrorConstants;
import com.example.ModernBankPLC.exception.BusinessValidationException;
import com.example.ModernBankPLC.exception.InvalidAccoutNumberException;
import com.example.ModernBankPLC.model.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Data
@NoArgsConstructor
public class AccountsRepo implements IAccountsRepo {

    @Autowired
    InternalInMemoryDbClass internalInMemoryDB;

    static Map<Integer, Account> internalAccountsDB;

    public AccountsRepo(InternalInMemoryDbClass internalInMemoryDB) {
        this.internalInMemoryDB = internalInMemoryDB;
    }

    /**
     * Method to fetch the account details based on the accountId
     *
     * @param accountId
     * @return Optional and nullable account details from the in-memory database
     */
    @Override
    public Optional<Account> getAccountByAccountId(int accountId) {
        return Optional.ofNullable(accountsDB().get(accountId));
    }

    /**
     * Retrieves the account based on account Id from the repository and
     * deletes the account from the in memory account database by updating its active status to inactive
     *
     * @param accountId
     * @return Returns the deleted account details to the service layer after updating active status to false.
     */
    @Override
    public Account deleteAccountByAccountId(int accountId) {
        internalAccountsDB = accountsDB();
        if (!internalAccountsDB.containsKey(accountId)) {
            throw new InvalidAccoutNumberException(ErrorConstants.INVALID_ACCOUNT_NUMBER);
        }
        internalAccountsDB.get(accountId).setActive(false);  // Delete the account , in a way inactivate the account.
        return internalAccountsDB.get(accountId);
    }


    /**
     * Retrieves the list of all the accounts present in the in memory account database
     *
     * @return
     */
    @Override
    public List<Account> getAccountList() {

        return accountsDB().values().stream().collect(Collectors.toList());
    }


    /**
     * Adds the account to the accounts database that is received from the client
     *
     * @param account
     * @return
     */
    @Override
    public Account addAccount(Account account) {
        internalAccountsDB = accountsDB();
        if (internalAccountsDB.containsKey(account.getAccountId())) {
            throw new BusinessValidationException(ErrorConstants.ACCOUNT_ALREADY_EXISTS);
        }
        internalAccountsDB.put(account.getAccountId(), account);
        return account;
    }


    /**
     * Updates the account detail into the account database
     *
     * @param account
     * @return returns the updated account detail to the service
     */
    @Override
    public Account updateAccountDetails(Account account) {
        accountsDB().put(account.getAccountId(), account);
        return account;
    }

    private Map<Integer, Account> accountsDB() {
        return internalInMemoryDB.getAccountDB();
    }

}