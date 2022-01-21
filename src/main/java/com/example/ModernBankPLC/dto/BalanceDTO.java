package com.example.ModernBankPLC.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class BalanceDTO {

    @JsonProperty("account-id")
    private int accountId ;
    private double balance;
    private String currency;

}
