package com.example.ModernBankPLC.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Exception class when there is exception due to business validation failure
 */
@Slf4j
public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) {
        super(message);
        log.error(message);
    }

}
