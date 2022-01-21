package com.example.ModernBankPLC.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class Transaction {

    @JsonIgnore
    private int accountNumber;

    @JsonProperty("account-id")
    private int otherPartyAccountNumber ;
    @JsonProperty("amount")
    private double txnAmount;
    @JsonProperty("currency")
    private String txnCurrency;
    @JsonProperty("type")
    private String txnType;
    @JsonProperty("transaction-date")
    private LocalDateTime time;


}
