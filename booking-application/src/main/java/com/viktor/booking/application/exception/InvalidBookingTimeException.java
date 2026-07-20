package com.viktor.booking.application.exception;

public class InvalidBookingTimeException extends RuntimeException {

    public InvalidBookingTimeException(String message) {
        super(message);
    }
}