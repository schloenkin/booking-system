package com.viktor.booking.application.exception;

public class BookingTimeConflictException
        extends RuntimeException {

    public BookingTimeConflictException(Long serviceId) {
        super(
                "Bookable service is already booked "
                        + "for the requested time, service id: "
                        + serviceId
        );
    }
}
