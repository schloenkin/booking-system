package com.viktor.booking.application.service;

import com.viktor.booking.application.repository.BookableServiceRepository;
import com.viktor.booking.domain.model.BookableService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookableServiceService {

    private final BookableServiceRepository serviceRepository;

    public BookableServiceService(
            BookableServiceRepository serviceRepository
    ) {
        this.serviceRepository = serviceRepository;
    }

    public List<BookableService> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<BookableService> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public BookableService createService(
            String name,
            String description,
            int durationMinutes
    ) {
        BookableService service = new BookableService(
                null,
                name,
                description,
                durationMinutes,
                true
        );

        return serviceRepository.save(service);
    }

    public Optional<BookableService> activateServiceById(Long id) {
        return serviceRepository.updateActive(id, true);
    }

    public Optional<BookableService> deactivateServiceById(Long id) {
        return serviceRepository.updateActive(id, false);
    }
}
