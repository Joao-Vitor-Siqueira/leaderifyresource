package jv.example.leaderifyresource.exception;

import jakarta.servlet.http.HttpServletRequest;
import jv.example.leaderifyresource.record.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getResource(), ex.getMessage(),
                        Instant.now(),
                        request.getRequestURI())
                );
    }
}
