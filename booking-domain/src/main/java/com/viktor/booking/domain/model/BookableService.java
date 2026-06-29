package com.viktor.booking.domain.model;

public class BookableService {
    private Long id;
    private String name;
    private String description;
    private int durationMinutes;
    private boolean active;

    public BookableService(Long id, String name, String description, int durationMinutes, boolean active) {
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
