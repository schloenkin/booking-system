package com.viktor.booking.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.viktor.booking")
public class BookingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApiApplication.class, args);
    }
}