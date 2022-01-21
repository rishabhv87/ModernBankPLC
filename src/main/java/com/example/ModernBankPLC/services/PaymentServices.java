package com.example.ModernBankPLC.services;

import com.example.ModernBankPLC.exception.ApiRequestException;
import com.example.ModernBankPLC.exception.BusinessValidationException;
import com.example.ModernBankPLC.model.Account;
import com.example.ModernBankPLC.model.Payment;
import com.example.ModernBankPLC.model.Transaction;
import com.example.ModernBankPLC.repository.IAccountsRepo;
import com.example.ModernBankPLC.repository.ITransactionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PaymentServices {

    @Autowired
    private IAccountsRepo accountsRepo;

    @Autowired
    private ITransactionRepo transactionRepository;

    public void makePayment(Payment payment){

        Account debitAccount = accountsRepo.getAccountByAccountId(payment.getDebitAccountId());
        Account creditAccount = accountsRepo.getAccountByAccountId(payment.getCreditAccountId());
        validatePayment(payment, debitAccount, creditAccount);

        double paymentAmount = payment.getTxnAmount();
        debitAccount.setBalance(debitAccount.getBalance() - paymentAmount);
        creditAccount.setBalance(creditAccount.getBalance() + paymentAmount);

        if (transactionRepository.getTransactionListByAccountId(debitAccount.getAccountId()) ==null )
        {
            throw new ApiRequestException("Debtor Account not found in transaction db");
        }
        if (transactionRepository.getTransactionListByAccountId(creditAccount.getAccountId()) ==null )
        {
            throw new ApiRequestException("Creditor Account not found in transaction db");
        }

        transactionRepository.addTransactionForAccountId(debitAccount.getAccountId() ,
                new Transaction(debitAccount.getAccountId(),
                                creditAccount.getAccountId(),
                        paymentAmount,
                                debitAccount.getCurrency(),
                                "DEBIT",
                                LocalDateTime.now()));

        transactionRepository.addTransactionForAccountId(creditAccount.getAccountId(),
                new Transaction(creditAccount.getAccountId(),
                                debitAccount.getAccountId(),
                        paymentAmount,
                                debitAccount.getCurrency(),
                                "CREDIT",
                                LocalDateTime.now()));


    }

    private void validatePayment(Payment payment, Account debitAccount, Account creditAccount) {
        if(debitAccount == null){
            throw new ApiRequestException("Invalid debit account number");
        }
        if(!debitAccount.isActive()){
            throw new ApiRequestException("Cannot make payment from inactive debit account");
        }

        if(creditAccount == null){
            throw new ApiRequestException("Invalid credit account number");
        }
        if(!creditAccount.isActive()){
            throw new ApiRequestException("Cannot make payment to inactive credit account");
        }
        checkSufficientFund(payment.getTxnAmount(), debitAccount);

    }

    private void checkSufficientFund(Double transactionAmount, Account debitAccount) {
        if(debitAccount.getBalance() < transactionAmount) {
            log.error("Debit account do not have sufficient fund");
            throw new BusinessValidationException("Insufficient fund in the debit account");
        }
    }


}
