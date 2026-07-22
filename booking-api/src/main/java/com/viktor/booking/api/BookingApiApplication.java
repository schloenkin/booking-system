package com.viktor.booking.api;

import com.viktor.booking.infrastructure.persistence.entity.BookingEntity;
import com.viktor.booking.infrastructure.persistence.repository.BookingJpaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.viktor.booking")
@EntityScan(basePackageClasses = BookingEntity.class)
@EnableJpaRepositories(
        basePackageClasses = BookingJpaRepository.class
)
public class BookingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApiApplication.class, args);
    }
}