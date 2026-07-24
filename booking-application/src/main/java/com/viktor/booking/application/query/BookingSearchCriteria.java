package com.viktor.booking.application.query;

import com.viktor.booking.domain.enums.BookingStatus;

import java.time.LocalDateTime;

public record BookingSearchCriteria(
        BookingStatus status,
        Long userId,
        Long serviceId,
        LocalDateTime from,
        LocalDateTime to
) {
}
