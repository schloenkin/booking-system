package com.viktor.booking.infrastructure.persistence.mapper;

import com.viktor.booking.domain.model.BookableService;
import com.viktor.booking.infrastructure.persistence.entity.BookableServiceEntity;
import org.springframework.stereotype.Component;

@Component
public class BookableServiceMapper {

    public BookableServiceEntity toNewEntity(BookableService service) {
        if (service == null) {
            return null;
        }

        return new BookableServiceEntity(
                service.getName(),
                service.getDescription(),
                service.getDurationMinutes(),
                service.isActive()
        );
    }

    public BookableService toDomain(BookableServiceEntity entity) {
        if (entity == null) {
            return null;
        }

        return new BookableService(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDurationMinutes(),
                entity.isActive()
        );
    }
}
