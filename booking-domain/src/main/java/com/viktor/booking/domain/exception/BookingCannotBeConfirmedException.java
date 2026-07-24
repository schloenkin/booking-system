package com.viktor.booking.domain.exception;

import com.viktor.booking.domain.enums.BookingStatus;

public class BookingCannotBeConfirmedException
        extends RuntimeException {

    public BookingCannotBeConfirmedException(
            BookingStatus currentStatus
    ) {
        super(
                "Booking cannot be confirmed from status: "
                        + currentStatus
        );
    }
}
