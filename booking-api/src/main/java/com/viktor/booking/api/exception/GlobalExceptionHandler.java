package com.viktor.booking.api.exception;

import com.viktor.booking.api.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.viktor.booking.domain.enums.BookingStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException exception,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getStatusCode().value(),
                exception.getStatusCode().toString(),
                exception.getReason(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {
        String allowedStatuses = Arrays.stream(BookingStatus.values())
                .map(Enum::name)
                .toList()
                .toString();

        String message = "Invalid booking status. Allowed values: " + allowedStatuses;

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

}