package com.example.ModernBankPLC.repository;

import com.example.ModernBankPLC.model.Transaction;

import java.util.List;

public interface ITransactionRepo {

    public List<Transaction>  getTransactionListByAccountId(int accountId);

    public List<Transaction> addTransactionForAccountId(int accountId, Transaction transaction);


}
