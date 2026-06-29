package com.viktor.booking.domain.model;
import com.viktor.booking.domain.enums.UserRole;

public class User {
    private Long id;
    private String email;
    private String passwordHash;
    private UserRole role;

    public User(Long id, String email, String passwordHash, UserRole role) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }
}
