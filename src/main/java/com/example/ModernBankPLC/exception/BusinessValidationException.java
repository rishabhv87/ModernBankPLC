package com.example.ModernBankPLC.exception;

/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Exception class when there is exception due to business validation failure
 */
public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) {
        super(message);
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
