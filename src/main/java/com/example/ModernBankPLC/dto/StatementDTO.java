package com.example.ModernBankPLC.dto;

import com.example.ModernBankPLC.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 *
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Statement DTO Returned to the customer with list of transaction
 *
 */

import java.util.List;

@Data
@AllArgsConstructor
public class StatementDTO {

    private List<Transaction> transactionList;

}
