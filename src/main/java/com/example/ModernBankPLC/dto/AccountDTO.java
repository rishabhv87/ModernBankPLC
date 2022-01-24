package com.example.ModernBankPLC.dto;

import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.model.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * DTO class will be used to communicate between one service to another service or client
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    @JsonProperty("account-id")
    private int accountId ;
    private BigDecimal balance;
    private String currency;


    @JsonProperty("isAccountActive")
    private boolean active;

    public AccountDTO(Account account) {
        this.accountId = account.getAccountId();
        this.balance = account.getBalance();
        this.currency = account.getCurrency();
        this.active = account.isActive();
    }

    public AccountDTO(int accountId , BigDecimal balance , String currency) {
        this.accountId = accountId;
        this.balance = balance;
        this.currency = currency;
    }



}
