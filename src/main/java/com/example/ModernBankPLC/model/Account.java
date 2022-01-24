package com.example.ModernBankPLC.model;


import com.example.ModernBankPLC.dto.AccountDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * This class is a model for the accounts database
 */
@Data
@AllArgsConstructor
public class Account {

    @JsonProperty("account-id")
    private int accountId ;
    private BigDecimal balance;
    private String currency;
    private boolean active;

    public Account(AccountDTO accountDTO) {
        this.accountId = accountDTO.getAccountId();
        this.balance =accountDTO.getBalance();
        this.currency = accountDTO.getCurrency();
        this.active = accountDTO.isActive();
    }
}
