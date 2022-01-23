package com.example.ModernBankPLC.services;

import com.example.ModernBankPLC.constants.ErrorConstants;
import com.example.ModernBankPLC.dto.AccountDTO;
import com.example.ModernBankPLC.dto.PaymentResponse;
import com.example.ModernBankPLC.exception.InvalidAccoutNumberException;
import com.example.ModernBankPLC.exception.BusinessValidationException;
import com.example.ModernBankPLC.model.PaymentRequest;
import com.example.ModernBankPLC.model.Transaction;
import com.example.ModernBankPLC.repository.IAccountsRepo;
import com.example.ModernBankPLC.repository.ITransactionRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.Math.round;

/**
 * Service class to make payment by updating the account model and the transaction model .
 *
 * @author : Rishabh Vishwakarma
 * @version : 1.0.0
 *
 */

@Slf4j
@Data
@AllArgsConstructor
@Service
public class PaymentServices {

    @Autowired
    private IAccountsRepo accountsRepo;

    @Autowired
    private AccountServices accountService;

    @Autowired
    private ITransactionRepo transactionRepository;


    /**
     * This method receives the payment detail from the client with debit account, credit account and transaction ammount.
     * Validates if payments accounts are valid and active and has sufficient fund to make payment
     * In the last it adds the new transaction to the transaction history in transaction repository
     *
     *
     * @param paymentRequest
     * @return Returns the Payment DTO object to client with the details such as transacting account-ids ,
     * transaction amount and the new transactionid
     */
    public PaymentResponse makePayment(PaymentRequest paymentRequest){

        AccountDTO debitAccount = accountService.getAccount(paymentRequest.getDebitAccountId()); //Exception will be thrown by account service
        AccountDTO creditAccount = accountService.getAccount(paymentRequest.getCreditAccountId()); // if debit and credit accountId doesnt exist
        BigDecimal paymentAmount = paymentRequest.getTxnAmount();

        //Validate several checks on the accounts and amounts
        validatePayment(paymentRequest, debitAccount, creditAccount);

        //Update debit and credit  account balance
        log.info("Its a valid payment. Updating the balance of debit and credit account");
        debitAccount.setBalance(debitAccount.getBalance().subtract(paymentAmount));
        creditAccount.setBalance(creditAccount.getBalance().add(paymentAmount));

        //Update the accounts details in the repository
        accountService.updateAccountInRepository(debitAccount);
        accountService.updateAccountInRepository( creditAccount);

        //Generating new transaction id for customer
        String newTransactionId = UUID.randomUUID().toString();
        log.info("TransactionId for the payment is {}" , newTransactionId);

        // Add payment transaction to the transaction table for both debit and credit accounts
        Transaction debitAccountNewTransactionEntry = new Transaction(newTransactionId,
                debitAccount.getAccountId(),
                creditAccount.getAccountId(),
                paymentAmount,
                debitAccount.getCurrency(),
                "DEBIT",
                LocalDateTime.now());

        Transaction creditAccountNewTransactionEntry = new Transaction(newTransactionId,
                creditAccount.getAccountId(),
                debitAccount.getAccountId(),
                paymentAmount,
                debitAccount.getCurrency(),
                "CREDIT",
                LocalDateTime.now());

        // Adding the transaction details into the repository for that account
        transactionRepository.addTransactionForAccountId(debitAccount.getAccountId() ,
                debitAccountNewTransactionEntry);
        transactionRepository.addTransactionForAccountId(creditAccount.getAccountId(),
                creditAccountNewTransactionEntry);

        return new PaymentResponse(paymentRequest, newTransactionId);

    }


    /**
     *  It also does the validation on the payment details , like if the debit and credit accounts are valid and active accounts
     *  It also performs business validations such as if the debit account has sufficient balance to make the payment of
     *  certain amount
     *  The method throws the exception to the client if any of the validations fail.
     *
     *
     * @param paymentRequest
     * @param debitAccount
     * @param creditAccount
     */
    private void validatePayment(PaymentRequest paymentRequest, AccountDTO debitAccount, AccountDTO creditAccount) {
        if(debitAccount== null){
            log.error(ErrorConstants.INVALID_DEBIT_ACCOUNT_NUMBER);
            throw new InvalidAccoutNumberException(ErrorConstants.INVALID_DEBIT_ACCOUNT_NUMBER);
        }
        if(creditAccount== null){
            log.error(ErrorConstants.INVALID_CREDIT_ACCOUNT_NUMBER);
            throw new InvalidAccoutNumberException(ErrorConstants.INVALID_CREDIT_ACCOUNT_NUMBER);
        }
        if(!debitAccount.isActive()){
            log.error(ErrorConstants.INACTIVE_DEBIT_ACCOUNT);
            throw new InvalidAccoutNumberException(ErrorConstants.INACTIVE_DEBIT_ACCOUNT);
        }
        if(!creditAccount.isActive()){
            log.error(ErrorConstants.INACTIVE_CREDIT_ACCOUNT);
            throw new InvalidAccoutNumberException(ErrorConstants.INACTIVE_CREDIT_ACCOUNT);
        }
        //Validate if the accountId is present in transaction table
        transactionRepository.getTransactionListByAccountId(debitAccount.getAccountId())
                .orElseThrow(() -> {throw new InvalidAccoutNumberException(ErrorConstants.DEBTOR_ACCOUNT_NOT_FOUND_IN_TRANSACTION_DB);}) ;
        transactionRepository.getTransactionListByAccountId(creditAccount.getAccountId())
                .orElseThrow(() -> {throw new InvalidAccoutNumberException(ErrorConstants.CREDITOR_ACCOUNT_NOT_FOUND_IN_TRANSACTION_DB);}) ;
        checkSufficientFund(paymentRequest.getTxnAmount(), debitAccount);

    }

    /**
     * Validates if the account has sufficient balance to make the payment
     *
     * @param transactionAmount
     * @param debitAccount
     */
    private void checkSufficientFund(BigDecimal transactionAmount, AccountDTO debitAccount) {
        if(debitAccount.getBalance().compareTo(transactionAmount) < 0) {
            log.error(ErrorConstants.DO_NOT_HAVE_SUFFICIENT_FUND);
            throw new BusinessValidationException(ErrorConstants.DO_NOT_HAVE_SUFFICIENT_FUND);
        }
    }


}
