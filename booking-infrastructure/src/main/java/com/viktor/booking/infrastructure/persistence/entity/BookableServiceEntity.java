package com.viktor.booking.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookable_services")
public class BookableServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            length = 150
    )
    private String name;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "duration_minutes",
            nullable = false
    )
    private int durationMinutes;

    @Column(
            name = "active",
            nullable = false
    )
    private boolean active;

    protected BookableServiceEntity(){

    }
    public BookableServiceEntity(
            String name,
            String description,
            int durationMinutes,
            boolean active
    ) {
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
