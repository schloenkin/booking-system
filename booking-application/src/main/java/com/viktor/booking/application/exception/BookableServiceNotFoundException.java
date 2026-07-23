package com.viktor.booking.application.exception;

public class BookableServiceNotFoundException extends RuntimeException {

    public BookableServiceNotFoundException(Long serviceId) {
        super("Bookable service not found with id: " + serviceId);
    }
}
