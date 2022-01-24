package com.example.ModernBankPLC.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Exception class when there is exception due to input from client such as invalid account number
 */

@Slf4j
public class InvalidAccoutNumberException extends RuntimeException {
    public InvalidAccoutNumberException(String message) {
        super(message);
        log.error(message);
    }

}
