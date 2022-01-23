package com.example.ModernBankPLC.controller;


import com.example.ModernBankPLC.dto.AccountDTO;
import com.example.ModernBankPLC.dto.BalanceDTO;
import com.example.ModernBankPLC.dto.StatementDTO;
import com.example.ModernBankPLC.services.AccountServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Controller class to receive the request from the request related to accounts , balance
 * and statement services
 */
@Slf4j
@RestController
@RequestMapping(value = "/accounts", produces = "application/json")
public class AccountController {

    @Autowired
    AccountServices accountService ;


    /**
     *
     * @return Returns the list of all the accounts that exists in the bank repository
     */
    @GetMapping( value = "/")
    public ResponseEntity<List<AccountDTO>> getAccountList(){
        log.info("On getaccountList");
        List<AccountDTO> accountList = accountService.getAccountList();
        log.info("Returning account list : {}", accountList);
        return ResponseEntity.ok(accountList);
    }


    /**
     *
     * @param accountId for which account details has to be retrieved
     * @return Returns the account details for the account id sent by the customer
     */
    @GetMapping( value = "/{accountId}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable(value = "accountId") int accountId){
        log.info("On /accounts/{accountId}");
        AccountDTO accountDTO = accountService.getAccount(accountId);
        log.info("Account retrieval sucessful : {}", accountDTO);
        return ResponseEntity.ok(accountDTO);
    }

    /**
     * Accepts the new account from client and adds it into bank repository
     *
     * @return Adds a added entry of the account into the bank repository
     */
    @PostMapping( value = "/addAccount")
    public ResponseEntity<AccountDTO> addAccount(@RequestBody AccountDTO accountDTO){
        log.info("On /accounts/addAccount");
        AccountDTO addedAccount = accountService.addAccount(accountDTO);
        log.info("Account was added succesfully : {}", addedAccount);
        return ResponseEntity.ok(addedAccount);
    }

    /**
     *
     * @param accountId for which balance has to be retrieved
     * @return Returns the balance for the account id sent by the customer
     */
    @GetMapping( value = "/{accountId}/balance")
    public ResponseEntity<BalanceDTO> getAccountBalance(@PathVariable(value = "accountId") int accountId){
        log.info("On /accounts/{accountId}/balance");
        BalanceDTO balanceDTO = accountService.getBalance(accountId);
        log.info("Balance retrieval was successful : {}", balanceDTO);
        return ResponseEntity.ok(balanceDTO);
    }


    /**
     *  Accepts the accountId from the client and returns ministatement containing limited transaction list
     *
     * @param accountId for which ministatement has to be retrieved
     * @return Returns the statement for the account id sent by the customer
     */
    @GetMapping( value = "/{accountId}/statements/mini")
    public ResponseEntity<StatementDTO> getAccountMiniStatement(@PathVariable(value = "accountId") int accountId){
        log.info("On /accounts/{accountId}/statements/mini");
        StatementDTO statement = accountService.getMiniStatement(accountId);
        log.info("Statment received : {} " ,statement.toString());
        return ResponseEntity.ok(statement);
    }

    /**
     * Accepts the accountId from client and deletes or inactivate the account.
     *
     * @param accountId for which account details has to be deleted
     * @return Returns the account details for the account id sent by the customer
     */

    @DeleteMapping( value = "delete/{accountId}")
    public ResponseEntity<AccountDTO> deleteAccount(@PathVariable(value = "accountId") int accountId){
        log.info("On /accounts/delete/{accountId}");
        AccountDTO deletedAccount = accountService.deleteAccount(accountId);
        log.info("Account was deleted successfully : {}", deletedAccount);
        return ResponseEntity.ok(deletedAccount);
    }


}
