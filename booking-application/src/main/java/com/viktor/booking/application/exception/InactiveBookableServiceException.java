package com.viktor.booking.application.exception;

public class InactiveBookableServiceException extends RuntimeException {
    public InactiveBookableServiceException(Long serviceId) {
        super("Bookable service is inactive with id: " + serviceId);
    }
}
