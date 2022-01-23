package com.example.ModernBankPLC.exception;

/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Exception class when there is exception due to input from client such as invalid account number
 */
public class InvalidAccoutNumberException extends RuntimeException {
    public InvalidAccoutNumberException(String message) {
        super(message);
    }

    public InvalidAccoutNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}
