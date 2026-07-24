package com.viktor.booking.application.query;

public record PageRequestData(
        int page,
        int size,
        String sortBy,
        String direction
) {
}
