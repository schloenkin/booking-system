package com.viktor.booking.api.controller;

import com.viktor.booking.api.dto.UserCreateRequest;
import com.viktor.booking.api.dto.UserResponse;
import com.viktor.booking.application.service.UserService;
import com.viktor.booking.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request
    ) {
        User createdUser = userService.createUser(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity
                .status(201)
                .body(toResponse(createdUser));
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("id") Long id
    ) {
        return userService.getUserById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}