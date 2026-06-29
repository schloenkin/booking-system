package com.viktor.booking.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class BookingCreateRequest {

    @NotNull(message = "User id must not be null")
    private Long userId;

    @NotNull(message = "Service id must not be null")
    private Long serviceId;

    @NotNull(message = "Start time must not be null")
    private LocalDateTime startTime;

    @NotNull(message = "End time must not be null")
    private LocalDateTime endTime;

    public Long getUserId() {
        return userId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}