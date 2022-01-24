package com.example.ModernBankPLC.exception;

/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Exception handler class . ALl the custom exceptions are handled by this class
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;



@ControllerAdvice
public class ApiExceptionHandler {


    /**
     * Handles the invalid account number exception
     *
     * @param invalidAccoutNumberException
     * @return Returns the exception object to client when input from client throws InvalidAccoutNumberException
     */
    @ExceptionHandler(value = {InvalidAccoutNumberException.class})
    public ResponseEntity<ApiException> handleApiRequestException(InvalidAccoutNumberException invalidAccoutNumberException) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                invalidAccoutNumberException.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);

    }


    /**
     * Handles the invalid account number exception
     *
     * @param businessValidationException exception thrown by application when there is business validation failure
     * @return Returns the exception object to client when input from client throws BusinessValidationException
     */
    @ExceptionHandler(value = {BusinessValidationException.class})
    public ResponseEntity<ApiException> handleBusinessValidationException(BusinessValidationException businessValidationException) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                businessValidationException.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);

    }

}
