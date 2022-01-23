package com.example.ModernBankPLC.repository;

import com.example.ModernBankPLC.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface ITransactionRepo {

    public Optional<List<Transaction>> getTransactionListByAccountId(int accountId);

    public Optional<Transaction> addTransactionForAccountId(int accountId, Transaction transaction);


}
