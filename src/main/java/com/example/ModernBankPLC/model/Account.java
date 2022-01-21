package com.example.ModernBankPLC.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Account {

    @JsonProperty("account-id")
    private int accountId ;
    private double balance;
    private String currency;
    private boolean active;

    /*@JsonIgnore
    private List<Transaction> transactionDetails;*/

    public Account(int accountId, double balance, String currency) {
        this.accountId = accountId;
        this.balance = balance;
        this.currency = currency;
    }
}
