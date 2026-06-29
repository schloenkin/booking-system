package com.viktor.booking.api.dto;

import java.time.LocalDateTime;

public class BookingResponse {

    private Long id;
    private Long userId;
    private Long serviceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    public BookingResponse(Long id, Long userId, Long serviceId,
                           LocalDateTime startTime, LocalDateTime endTime,
                           String status) {
        this.id = id;
        this.userId = userId;
        this.serviceId = serviceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

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

    public String getStatus() {
        return status;
    }
}