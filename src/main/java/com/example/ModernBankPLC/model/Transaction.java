package com.example.ModernBankPLC.model;

/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Transaction model that is stored in the repository and cal also be passed to customer.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class Transaction {

    @JsonIgnore
    private String transactionId;

    @JsonIgnore
    private int accountNumber;

    @JsonProperty("account-id")
    private int otherPartyAccountNumber ;
    @JsonProperty("amount")
    private BigDecimal txnAmount;
    @JsonProperty("currency")
    private String txnCurrency;
    @JsonProperty("type")
    private String txnType;
    @JsonProperty("transaction-date")
    private LocalDateTime time;


}
