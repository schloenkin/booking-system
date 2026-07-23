package com.viktor.booking.application.exception;

public class InvalidBookingDurationException extends RuntimeException {
    public InvalidBookingDurationException(int expectedDurationMinutes) {
        super("Booking duration must be exactly "
                + expectedDurationMinutes
                + " minutes"
        );
    }
}
