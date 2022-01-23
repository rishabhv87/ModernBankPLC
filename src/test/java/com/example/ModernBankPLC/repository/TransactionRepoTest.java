package com.example.ModernBankPLC.repository;

import com.example.ModernBankPLC.dto.BalanceDTO;
import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionRepoTest {
    private static Map<Integer , List<Transaction>> mockedTransactionDB= new HashMap<>();

    static {
        mockedTransactionDB.put(111, new LinkedList<Transaction>(Arrays.asList(
                new Transaction("txnId-1", 111, 444, new BigDecimal(10), "GBP", "DEBIT", LocalDateTime.of(2021, Month.APRIL,21,10,37,50)),
                new Transaction("txnId-2", 111, 555, new BigDecimal(10), "GBP", "CREDIT", LocalDateTime.of(2021,Month.FEBRUARY,3,6,30,40))
                ))

        );
        mockedTransactionDB.put(222, new LinkedList<Transaction>(Arrays.asList(
                new Transaction("txnId-3", 222, 444, new BigDecimal(10), "GBP", "DEBIT", LocalDateTime.of(2021, Month.APRIL,21,10,37,50)),
                new Transaction("txnId-4", 222, 555, new BigDecimal(10), "GBP", "CREDIT", LocalDateTime.of(2021,Month.FEBRUARY,3,6,30,40))
                ))

        );
  }


    @Mock
    private InternalInMemoryDbClass internalInMemoryDB;

    @Test
    public void shouldGetTransactionListByAccount(){
        ITransactionRepo transactionRepo = new TransactionRepo(internalInMemoryDB);
        Mockito.when(internalInMemoryDB.getTransactionListDB()).thenReturn(mockedTransactionDB);
        Optional<List<Transaction>> transactionList = transactionRepo.getTransactionListByAccountId(111);
        assertNotNull( transactionList.get());
        assertEquals(2, transactionList.get().size());

        Transaction firstTransaction = transactionList.get().get(0);
        assertEquals(444, firstTransaction.getOtherPartyAccountNumber());
        assertEquals(new BigDecimal(10), firstTransaction.getTxnAmount());
        assertEquals("DEBIT", firstTransaction.getTxnType());
        assertEquals("GBP", firstTransaction.getTxnCurrency());

    }


    @Test
    public void shouldAddTransactionByAccount(){
        ITransactionRepo transactionRepo = new TransactionRepo(internalInMemoryDB);
        Mockito.when(internalInMemoryDB.getTransactionListDB()).thenReturn(mockedTransactionDB);
        Transaction newTransaction = new Transaction("txnId-5", 222, 555, new BigDecimal(5), "GBP", "DEBIT", LocalDateTime.of(2021, Month.DECEMBER,2,7,37,50));
        Optional<Transaction> insertedTransaction = transactionRepo.addTransactionForAccountId(222,newTransaction);
        assertNotNull( insertedTransaction.get());
        assertEquals(222, insertedTransaction.get().getAccountNumber());
        assertEquals(555, insertedTransaction.get().getOtherPartyAccountNumber());
        assertEquals(new BigDecimal(5), insertedTransaction.get().getTxnAmount());
        assertEquals("DEBIT", insertedTransaction.get().getTxnType());
        assertEquals("GBP", insertedTransaction.get().getTxnCurrency());

    }
}