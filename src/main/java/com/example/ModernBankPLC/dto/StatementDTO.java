package com.example.ModernBankPLC.dto;

import com.example.ModernBankPLC.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatementDTO {

    private List<Transaction> transactionList;

}
