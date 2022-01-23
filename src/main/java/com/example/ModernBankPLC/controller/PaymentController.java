package com.example.ModernBankPLC.controller;


import com.example.ModernBankPLC.dto.PaymentResponse;
import com.example.ModernBankPLC.exception.BusinessValidationException;
import com.example.ModernBankPLC.exception.InvalidAccoutNumberException;
import com.example.ModernBankPLC.model.PaymentRequest;
import com.example.ModernBankPLC.services.AccountServices;
import com.example.ModernBankPLC.services.PaymentServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class to serve the requests related to payment processing between different account
 *
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 */


@Slf4j
@RestController
@RequestMapping(value = "/payments", produces = "application/json")
public class PaymentController {


    @Autowired
    PaymentServices paymentServices ;



    @PostMapping( value = "/makePayment")
    public ResponseEntity<PaymentResponse> makePayment(@RequestBody PaymentRequest paymentRequest){
        log.info("Paymnent request received : {}" , paymentRequest);

        if (paymentRequest.getDebitAccountId()==0 || paymentRequest.getCreditAccountId()== 0) {
            log.error("Invalid or missing account numbers");
            throw new InvalidAccoutNumberException("Invalid or missing account numbers");
        }
        if (paymentRequest.getTxnAmount()==null || paymentRequest.getTxnAmount().doubleValue()== 0){
            log.error("Transaction amount missing or is 0");
            throw new BusinessValidationException("Transaction amount missing or is 0");
        }
        PaymentResponse paymentResponse = paymentServices.makePayment(paymentRequest);
        log.info("Payment was succefull. Returning response : {}" , paymentResponse);
        return ResponseEntity.ok(paymentResponse);
    }






}
