package com.example.ModernBankPLC.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Payment {

    private int debitAccountId;
    private int creditAccountId;
    private double txnAmount;
}
