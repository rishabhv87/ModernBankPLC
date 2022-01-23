package com.example.ModernBankPLC.repository;

import com.example.ModernBankPLC.exception.InvalidAccoutNumberException;
import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.model.Transaction;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@NoArgsConstructor
public class TransactionRepo implements ITransactionRepo{

    @Autowired
    InternalInMemoryDbClass internalInMemoryDB;

    static Map<Integer, Account> internalTransactionDB;

    /*private static Map<Integer , LinkedList<Transaction>> accountTransactionsMap = new HashMap<>();
    static {
        accountTransactionsMap.put(111, new LinkedList<Transaction>());
        accountTransactionsMap.put(222, new LinkedList<Transaction>());
        accountTransactionsMap.put(333, new LinkedList<Transaction>());
        accountTransactionsMap.put(444, new LinkedList<Transaction>());
        accountTransactionsMap.put(555, new LinkedList<Transaction>());
    }*/


    public TransactionRepo(InternalInMemoryDbClass internalInMemoryDB) {
        this.internalInMemoryDB = internalInMemoryDB;
    }

    @Override
    public Optional<List<Transaction>> getTransactionListByAccountId(int accountId) {
        return Optional.ofNullable(transactionDB().get(accountId));
    }

    @Override
    public Optional<Transaction> addTransactionForAccountId(int accountId , Transaction transaction) {

        getTransactionListByAccountId(accountId)
                .ifPresentOrElse(transactions -> {transactions.add(0,transaction);},
                        () -> {throw new InvalidAccoutNumberException("Invalid Account Number");});
        return Optional.of(transaction);

}

    private Map<Integer, List<Transaction>> transactionDB() {
        return internalInMemoryDB.getTransactionListDB();
    }

}
