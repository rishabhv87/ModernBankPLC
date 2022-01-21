package com.example.ModernBankPLC.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ApiException {
    private HttpStatus status;
    private String message;
    private LocalDateTime timestamp;

}
