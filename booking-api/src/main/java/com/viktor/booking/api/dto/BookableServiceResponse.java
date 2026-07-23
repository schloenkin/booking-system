package com.viktor.booking.api.dto;

public class BookableServiceResponse {

    private Long id;
    private String name;
    private String description;
    private int durationMinutes;
    private boolean active;

    public BookableServiceResponse(
            Long id,
            String name,
            String description,
            int durationMinutes,
            boolean active
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public boolean isActive() {
        return active;
    }
}
