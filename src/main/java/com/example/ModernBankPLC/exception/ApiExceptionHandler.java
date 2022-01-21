package com.example.ModernBankPLC.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<ApiException> handleApiRequestException(ApiRequestException apiRequestException) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST,
                apiRequestException.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = {BusinessValidationException.class})
    public ResponseEntity<ApiException> handleBusinessValidationException(BusinessValidationException businessValidationException) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST,
                businessValidationException.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);

    }

}
