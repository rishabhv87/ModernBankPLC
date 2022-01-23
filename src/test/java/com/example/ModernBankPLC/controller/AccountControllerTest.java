package com.example.ModernBankPLC.controller;

import com.example.ModernBankPLC.dto.AccountDTO;
import com.example.ModernBankPLC.dto.BalanceDTO;
import com.example.ModernBankPLC.dto.StatementDTO;
import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.model.Transaction;
import com.example.ModernBankPLC.services.AccountServices;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {

    @MockBean
    AccountServices accountService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAccountList() throws Exception {
        AccountDTO accountDTO1 = new AccountDTO(111, new BigDecimal(500.00) , "GBP" , true);
        AccountDTO accountDTO2 = new AccountDTO(222, new BigDecimal(500.00) , "GBP" , true);
        Mockito.when(accountService.getAccountList()).thenReturn(Arrays.asList(accountDTO1, accountDTO2));

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].account-id", Matchers.is(111)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance", Matchers.is(500)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currency", Matchers.is("GBP")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isAccountActive", Matchers.is(true)));
    }

    @Test
    void getAccountById() throws Exception {
        AccountDTO accountDTO1 = new AccountDTO(111, new BigDecimal(500.00) , "GBP" , true);
        Mockito.when(accountService.getAccount(Mockito.any(Integer.class))).thenReturn(accountDTO1);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/111"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account-id", Matchers.is(111)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(500)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency", Matchers.is("GBP")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAccountActive", Matchers.is(true)));
    }


    @Test
    void getBalanceById() throws Exception {
        BalanceDTO balanceDTO1 = new BalanceDTO(111, new BigDecimal(500.00) , "GBP");
        Mockito.when(accountService.getBalance(Mockito.any(Integer.class))).thenReturn(balanceDTO1);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/111/balance"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account-id", Matchers.is(111)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(500)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency", Matchers.is("GBP")));
    }

    @Test
    void addAccount() throws Exception {
        AccountDTO accountDTO1 = new AccountDTO(666, new BigDecimal(500.00) , "GBP" , true);
        Mockito.when(accountService.addAccount(Mockito.any(AccountDTO.class))).thenReturn(accountDTO1);

        String requestBody = "{\n" +
                "    \"account-id\": 666,\n" +
                "    \"balance\": 500.0,\n" +
                "    \"currency\": \"GBP\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/addAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account-id", Matchers.is(666)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(500)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency", Matchers.is("GBP")));
    }


    @Test
    void getAccountMiniStatement() throws Exception {

        Transaction transaction1 = new Transaction("txnId-1", 333, 444, new BigDecimal(10), "GBP", "DEBIT", LocalDateTime.of(2021,Month.APRIL,21,10,37,50));
        Transaction transaction2 = new Transaction("txnId-1", 333, 555, new BigDecimal(10), "GBP", "CREDIT", LocalDateTime.of(2021,Month.FEBRUARY,3,6,30,40));
        Mockito.when(accountService.getMiniStatement(Mockito.any(Integer.class))).thenReturn(new StatementDTO(Arrays.asList(transaction1, transaction2)));

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/111/statements/mini"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionList[0].account-id", Matchers.is(444)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionList[0].amount", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionList[0].currency", Matchers.is("GBP")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionList[0].type", Matchers.is("DEBIT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionList[0].transaction-date", Matchers.is(LocalDateTime.of(2021,Month.APRIL,21,10,37,50).toString())));
    }

    @Test
    void deleteAccount() throws Exception {

        AccountDTO accountDTO1 = new AccountDTO(111, new BigDecimal(500.00) , "GBP" , true);

        String requestBody = "{\n" +
                "    \"account-id\": 111,\n" +
                "    \"balance\": 500.00,\n" +
                "    \"currency\": \"GBP\"\n" +
                "}";

        Mockito.when(accountService.deleteAccount(Mockito.any(Integer.class))).thenReturn(accountDTO1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/delete/111")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account-id", Matchers.is(111)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(500)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency", Matchers.is("GBP")));
    }
}