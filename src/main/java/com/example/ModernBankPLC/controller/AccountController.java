package com.example.ModernBankPLC.controller;


import com.example.ModernBankPLC.dto.AccountDTO;
import com.example.ModernBankPLC.dto.BalanceDTO;
import com.example.ModernBankPLC.exception.ApiRequestException;
import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.dto.StatementDTO;
import com.example.ModernBankPLC.services.AccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/accounts", produces = "application/json")
public class AccountController {

    @Autowired
    AccountServices accountService ;

    @GetMapping( value = "/")
    public ResponseEntity<List<AccountDTO>> getAccountList(){
        System.out.println("request received");
        accountService.getAccountList();
        return ResponseEntity.ok(accountService.getAccountList());
    }

    @GetMapping( value = "/{accountId}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable(value = "accountId") int accountId){
        System.out.println("request received");
        AccountDTO accountDTO = accountService.getAccount(accountId);
        return ResponseEntity.ok(accountDTO);
    }


    @PostMapping( value = "/addAccount")
    public ResponseEntity<AccountDTO> addAccount(@RequestBody AccountDTO accountDTO){
        System.out.println("request received");
        return ResponseEntity.ok( accountService.addAccount(accountDTO));
    }


    @GetMapping( value = "/{accountId}/balance")
    public ResponseEntity<BalanceDTO> getAccountBalance(@PathVariable(value = "accountId") int accountId){
        System.out.println("request received");
        BalanceDTO balanceDTO = accountService.getBalance(accountId);
        return ResponseEntity.ok(balanceDTO);
    }

    @GetMapping( value = "/{accountId}/statements/mini")
    public ResponseEntity<StatementDTO> getAccountMiniStatement(@PathVariable(value = "accountId") int accountId){
        System.out.println("request received");
        StatementDTO statement = accountService.getMiniStatement(accountId);

        return ResponseEntity.ok(statement);
    }

    @DeleteMapping( value = "delete/{accountId}")
    public ResponseEntity<AccountDTO> deleteAccount(@PathVariable(value = "accountId") int accountId){
        System.out.println("request received");
        return ResponseEntity.ok( accountService.deleteAccount(accountId));
    }


}
