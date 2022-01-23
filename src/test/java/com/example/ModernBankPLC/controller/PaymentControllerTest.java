package com.example.ModernBankPLC.controller;

import com.example.ModernBankPLC.dto.AccountDTO;
import com.example.ModernBankPLC.dto.PaymentResponse;
import com.example.ModernBankPLC.exception.BusinessValidationException;
import com.example.ModernBankPLC.exception.InvalidAccoutNumberException;
import com.example.ModernBankPLC.model.PaymentRequest;
import com.example.ModernBankPLC.services.PaymentServices;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {



    @MockBean
    PaymentServices paymentServices;

    @Autowired
    MockMvc mockMvc;

    @Test
    void makePayment() throws Exception {

        PaymentResponse paymentResponse = new PaymentResponse(333, 444 , new BigDecimal(10.04), "txnId-123456");
        Mockito.when(paymentServices.makePayment(Mockito.any(PaymentRequest.class))).thenReturn(paymentResponse);

        String requestBody = "{\n" +
                "\t\"debitAccountId\" : 333,\n" +
                "\t\"creditAccountId\" : 444,\n" +
                "\t\"txnAmount\" : 1.04\n" +
                "\t\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/payments/makePayment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.debitAccountId", Matchers.is(333)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creditAccountId", Matchers.is(444)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.txnAmount", Matchers.is(new BigDecimal(10.04))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionId", Matchers.is("txnId-123456")));

    }

    @Test
    void makePaymentWithMissingDebitAccount() throws Exception {

        PaymentResponse paymentResponse = new PaymentResponse(333, 444 , new BigDecimal(10.00), "txnId-123456");
        Mockito.when(paymentServices.makePayment(Mockito.any(PaymentRequest.class))).thenReturn(paymentResponse);

        String requestBody = "{\n" +
                "\t\"creditAccountId\" : 444,\n" +
                "\t\"txnAmount\" : 1.04\n" +
                "\t\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/payments/makePayment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidAccoutNumberException))
                .andExpect(result -> assertEquals("Invalid or missing account numbers", result.getResolvedException().getMessage()));

    }


    @Test
    void makePaymentWithMissingCreditAccount() throws Exception {

        PaymentResponse paymentResponse = new PaymentResponse(333, 444 , new BigDecimal(10.00), "txnId-123456");
        Mockito.when(paymentServices.makePayment(Mockito.any(PaymentRequest.class))).thenReturn(paymentResponse);

        String requestBody = "{\n" +
                "\t\"debitAccountId\" : 333,\n" +
                "\t\"txnAmount\" : 1.04\n" +
                "\t\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/payments/makePayment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidAccoutNumberException))
                .andExpect(result -> assertEquals("Invalid or missing account numbers", result.getResolvedException().getMessage()));

    }

    @Test
    void makePaymentWithMissingTransactionAmount() throws Exception {

        PaymentResponse paymentResponse = new PaymentResponse(333, 444 , new BigDecimal(10.00), "txnId-123456");
        Mockito.when(paymentServices.makePayment(Mockito.any(PaymentRequest.class))).thenReturn(paymentResponse);

        String requestBody =  "{\n" +
                "\t\"debitAccountId\" : 333,\n" +
                "\t\"creditAccountId\" : 444\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/payments/makePayment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BusinessValidationException))
                .andExpect(result -> assertEquals("Transaction amount missing or is 0", result.getResolvedException().getMessage()));

    }

}