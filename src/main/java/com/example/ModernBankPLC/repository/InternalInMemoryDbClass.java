package com.example.ModernBankPLC.repository;

import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@Data
@AllArgsConstructor
public class InternalInMemoryDbClass {

        private Map<Integer , Account> accountDB;
        private Map<Integer , List<Transaction>> transactionListDB;
    
        /*public Map<Integer, Account> getAccountDB() {
            return accountDB;
        }

        public void setAccountDB(Map<Integer, Account> accountDB) {
            this.accountDB = accountDB;
        }*/

        

        public InternalInMemoryDbClass() {
            accountDB = new HashMap<>();
            accountDB.put(111, new Account(111, new BigDecimal(500.00) , "GBP", true));
            accountDB.put(222, new Account(222, new BigDecimal(500.00) , "GBP",true));
            accountDB.put(333, new Account(333, new BigDecimal(500.00) , "GBP", true));
            accountDB.put(444, new Account(444, new BigDecimal(500.00) , "GBP",true));
            accountDB.put(555, new Account(555, new BigDecimal(500.00) , "GBP", true));

            transactionListDB= new HashMap<>();
            transactionListDB.put(111, new LinkedList<Transaction>());
            transactionListDB.put(222, new LinkedList<Transaction>());
            transactionListDB.put(333, new LinkedList<Transaction>());
            transactionListDB.put(444, new LinkedList<Transaction>());
            transactionListDB.put(555, new LinkedList<Transaction>());
        }



}
