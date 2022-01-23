package com.example.ModernBankPLC.exception;
/**
 * @author Rishabh Vishwakarma
 * @version 1.0.0
 *
 * Exception class DTO that is sent to client
 */
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
