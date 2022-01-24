package com.example.ModernBankPLC.model;

/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Payment object sent by client in the http request
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentRequest {

    private int debitAccountId;
    private int creditAccountId;
    private BigDecimal txnAmount;
}
