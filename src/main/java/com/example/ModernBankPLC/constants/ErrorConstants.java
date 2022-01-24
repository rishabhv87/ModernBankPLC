package com.example.ModernBankPLC.constants;


/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Constants class with lists of the error code
 */
public class ErrorConstants {
    public static final String INVALID_ACCOUNT_NUMBER = "Invalid Account Number";
    public static final String INVALID_DEBIT_ACCOUNT_NUMBER = "Invalid Debit Account Number";
    public static final String INVALID_CREDIT_ACCOUNT_NUMBER = "Invalid Credit Account Number";
    public static final String INACTIVE_DEBIT_ACCOUNT = "Cannot make payment from inactive debit account";
    public static final String INACTIVE_CREDIT_ACCOUNT = "Cannot make payment to inactive credit account";
    public static final String DEBTOR_ACCOUNT_NOT_FOUND_IN_TRANSACTION_DB = "Debtor Account not found in transaction db";
    public static final String CREDITOR_ACCOUNT_NOT_FOUND_IN_TRANSACTION_DB = "Creditor Account not found in transaction db";
    public static final String DO_NOT_HAVE_SUFFICIENT_FUND = "Debit account do not have sufficient fund";
    public static final String ACCOUNT_ALREADY_EXISTS = "Account already exists in system";
}
