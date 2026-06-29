package com.viktor.booking.api.dto;

import java.time.LocalDateTime;

public class BookingCreateRequest {

    private Long userId;
    private Long serviceId;
    private LocalDateTime startTime;
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