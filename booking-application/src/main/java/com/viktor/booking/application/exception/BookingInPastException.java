package com.viktor.booking.application.exception;

public class BookingInPastException extends RuntimeException {

    public BookingInPastException() {
        super("Booking start time must be in the future");
    }
}
