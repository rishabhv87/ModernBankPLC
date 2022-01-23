package com.example.ModernBankPLC.services;

import com.example.ModernBankPLC.constants.ErrorConstants;
import com.example.ModernBankPLC.dto.AccountDTO;
import com.example.ModernBankPLC.dto.BalanceDTO;
import com.example.ModernBankPLC.exception.InvalidAccoutNumberException;
import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.dto.StatementDTO;
import com.example.ModernBankPLC.model.Transaction;
import com.example.ModernBankPLC.repository.IAccountsRepo;
import com.example.ModernBankPLC.repository.ITransactionRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to perform CRUD operations on the accounts and the transaction repository.
 *
 * @author : Rishabh Vishwakarma
 * @version : 1.0.0
 *
 */
@Service
@Slf4j
@Data
@AllArgsConstructor
public class AccountServices {
    
    private final int MAX_NUMBER_OF_TRANSACTIONS_PER_STATEMENT = 20;

    @Autowired
    private IAccountsRepo accountsRepo;

    @Autowired
    private ITransactionRepo transactionRepo;

    /**
     * This methods accepts a accountDTO object from client and add to the database .Assume here that customer sends the account-id also
     *
     * @param accountDTO which is collection account-id , balance and currency
     * @return Returns the same accountDTO object if the insertion of account is successful into the repository
     */
    public AccountDTO addAccount(AccountDTO accountDTO) {
        Account newAccount = new Account(accountDTO.getAccountId(), accountDTO.getBalance(), accountDTO.getCurrency(), true);
        Account insertedAccount= accountsRepo.addAccount(newAccount);
        return new AccountDTO(insertedAccount);
    }

    /**
     *
     * @return Returns the list of all account DTO objects from the database : account-id, balance , currency and the account active status
     */
    public List<AccountDTO> getAccountList() {
        List<Account> accountListFromDB = accountsRepo.getAccountList();
        return accountListFromDB.stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
    }

    /**
     *
     *
     * @param accountId for which account details needs to be retrieved.
     * @return Returns the account DTO object from database based on the account-id from the client
     */
    public AccountDTO getAccount(int accountId) {
        Account accountFromDB = accountsRepo.getAccountByAccountId(accountId)
                .orElseThrow(() -> new InvalidAccoutNumberException(ErrorConstants.INVALID_ACCOUNT_NUMBER));
        return new AccountDTO(accountFromDB.getAccountId(),
                                accountFromDB.getBalance(),
                                accountFromDB.getCurrency(),
                                accountFromDB.isActive());
    }

    /**
     *
     * @param accountId for which balance needs to be retrieved.
     * @return Returns the BalanceDTO based on the account id from the client . Balance DTO contains account-id , balance and currency
     */
    public BalanceDTO getBalance(int accountId) {
        Account accountWithBalance = accountsRepo.getAccountByAccountId(accountId)
                .orElseThrow(() -> new InvalidAccoutNumberException(ErrorConstants.INVALID_ACCOUNT_NUMBER));
        return new BalanceDTO(accountWithBalance.getAccountId(), accountWithBalance.getBalance(), accountWithBalance.getCurrency());
    }


    /**
     *
     * @param accountId for which transaction details needs to be retrieved.
     * @return Returns the mini statements of max 20 recent transactions based on the account id from the client
     */
    public StatementDTO getMiniStatement(int accountId) {

        List<Transaction> transactionListForAccountId = transactionRepo.getTransactionListByAccountId(accountId)
                .orElseThrow(() -> {throw new InvalidAccoutNumberException(ErrorConstants.INVALID_ACCOUNT_NUMBER);});

        List<Transaction> transactionList = transactionListForAccountId.size() >= MAX_NUMBER_OF_TRANSACTIONS_PER_STATEMENT
                ? transactionListForAccountId.subList(0, MAX_NUMBER_OF_TRANSACTIONS_PER_STATEMENT)
                : transactionListForAccountId;

        return new StatementDTO(transactionList);
    }

    /**
     * Calls the repository to deletes the account from active database. or set the status to inactive in the existing database
     * so that it is available to be fetched by user later , but no operations should be allowed on the delected/inactive account,
     * hence setting the active status to false for such accounts.
     *
     * @param accountId for which account has to be deleted or inactivated from repository
     * @return Returns the deleted account
     */
    public AccountDTO deleteAccount(int accountId) {
        Account accountFromDB = accountsRepo.deleteAccountByAccountId(accountId);
        if(accountFromDB == null){
            throw new InvalidAccoutNumberException(ErrorConstants.INVALID_ACCOUNT_NUMBER);
        }

        return new AccountDTO(accountFromDB);
    }


    /**
     * This method is used by services like account service and payment service to update the repository with the
     * latest status of account(such as after updating the account balance)
     *
     * @param accountDTO for which account details needs to be updated.
     * @return
     */
    public AccountDTO updateAccountInRepository(AccountDTO accountDTO) {
        Account accountFromDB = accountsRepo.getAccountByAccountId(accountDTO.getAccountId()).orElse(null);
        if(accountFromDB == null){
            throw new InvalidAccoutNumberException(ErrorConstants.INVALID_ACCOUNT_NUMBER);
        }

        Account account = new Account(accountDTO);
        accountsRepo.updateAccountDetails(account);
        return accountDTO;
    }

}
