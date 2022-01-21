package com.example.ModernBankPLC.repository;

import com.example.ModernBankPLC.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionRepo implements ITransactionRepo{

    private static Map<Integer , LinkedList<Transaction>> accountTransactionsMap = new HashMap<>();
    static {
        accountTransactionsMap.put(111, new LinkedList<Transaction>());
        accountTransactionsMap.put(222, new LinkedList<Transaction>());
        accountTransactionsMap.put(333, new LinkedList<Transaction>());
        accountTransactionsMap.put(444, new LinkedList<Transaction>());
        accountTransactionsMap.put(555, new LinkedList<Transaction>());
    }


    @Override
    public List<Transaction> getTransactionListByAccountId(int accountId) {
        List<Transaction> transactionList = accountTransactionsMap.get(accountId);
        return transactionList;
    }

    @Override
    public List<Transaction> addTransactionForAccountId(int accountId , Transaction transaction) {
        List<Transaction> transactionList = getTransactionListByAccountId(accountId);

        transactionList.add(0, transaction);
        return transactionList;
    }


}
