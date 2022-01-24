package com.example.ModernBankPLC.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Balance DTO to return customer with account-id , balance and currency
 */
@Data
@AllArgsConstructor
public class BalanceDTO {

    @JsonProperty("account-id")
    private int accountId ;
    private BigDecimal balance;
    private String currency;

}
