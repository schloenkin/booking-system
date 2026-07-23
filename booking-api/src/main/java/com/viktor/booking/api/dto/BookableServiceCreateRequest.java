package com.viktor.booking.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class BookableServiceCreateRequest {

    @NotBlank(message = "Name must not be blank")
    @Size(
            max = 150,
            message = "Name must not contain more than 150 characters"
    )
    private String name;

    @Size(
            max = 2000,
            message = "Description must not contain more than 2000 characters"
    )
    private String description;

    @Positive(message = "Duration must be greater than zero")
    private int durationMinutes;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
