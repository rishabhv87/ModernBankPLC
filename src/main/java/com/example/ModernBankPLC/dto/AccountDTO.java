package com.example.ModernBankPLC.dto;

import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.model.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountDTO {

    @JsonProperty("account-id")
    private int accountId ;
    private double balance;
    private String currency;
    @JsonProperty("isAccountActive")
    private boolean active;

    public AccountDTO(Account account) {
        this.accountId = account.getAccountId();
        this.balance = account.getBalance();
        this.currency = account.getCurrency();
        this.active = account.isActive();
    }


    /*public AccountDTO(int accountId, double balance, String currency, boolean active) {
        this.accountId = accountId;
        this.balance = balance;
        this.currency = currency;
        this.active = active;
    }*/

}
