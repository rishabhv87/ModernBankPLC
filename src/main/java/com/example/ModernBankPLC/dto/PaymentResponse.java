package com.example.ModernBankPLC.dto;

import com.example.ModernBankPLC.model.PaymentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Payment DTO that is returned to the client with accounts , amount and transaction id
 * after successful payment
 */

@Data
@AllArgsConstructor
public class PaymentResponse {

    private int debitAccountId;
    private int creditAccountId;
    private BigDecimal txnAmount;
    private String transactionId;

    public PaymentResponse(PaymentRequest paymentRequest, String transactionId){
        this.debitAccountId = paymentRequest.getDebitAccountId();
        this.creditAccountId = paymentRequest.getCreditAccountId();
        this.txnAmount= paymentRequest.getTxnAmount();
        this.transactionId = transactionId;
    }

}
