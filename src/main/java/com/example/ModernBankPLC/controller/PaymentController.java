package com.example.ModernBankPLC.controller;


import com.example.ModernBankPLC.exception.ApiRequestException;
import com.example.ModernBankPLC.exception.BusinessValidationException;
import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.model.Payment;
import com.example.ModernBankPLC.services.AccountServices;
import com.example.ModernBankPLC.services.PaymentServices;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/payments", produces = "application/json")
public class PaymentController {

    @Autowired
    AccountServices accountServices;

    @Autowired
    PaymentServices paymentServices ;

    @PostMapping( value = "/makePayment")
    public ResponseEntity<Payment> getAccountBalance(@RequestBody Payment payment){
        System.out.println("request received");

        if (payment.getDebitAccountId()==0 || payment.getCreditAccountId()== 0) {
            throw new ApiRequestException("Invalid or missing account numbers");
        }

        paymentServices.makePayment(payment);
        return ResponseEntity.ok(payment);
    }






}
