package com.viktor.booking.infrastructure.persistence.adapter;

import com.viktor.booking.application.repository.BookableServiceRepository;
import com.viktor.booking.domain.model.BookableService;
import com.viktor.booking.infrastructure.persistence.entity.BookableServiceEntity;
import com.viktor.booking.infrastructure.persistence.mapper.BookableServiceMapper;
import com.viktor.booking.infrastructure.persistence.repository.BookableServiceJpaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
@Transactional
public class JpaBookableServiceRepositoryAdapter
        implements BookableServiceRepository {

    private final BookableServiceJpaRepository serviceJpaRepository;
    private final BookableServiceMapper serviceMapper;

    public JpaBookableServiceRepositoryAdapter(
            BookableServiceJpaRepository serviceJpaRepository,
            BookableServiceMapper serviceMapper
    ) {
        this.serviceJpaRepository = serviceJpaRepository;
        this.serviceMapper = serviceMapper;
    }

    @Override
    public BookableService save(BookableService service) {
        BookableServiceEntity entity =
                serviceMapper.toNewEntity(service);

        BookableServiceEntity savedEntity =
                serviceJpaRepository.save(entity);

        return serviceMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookableService> findById(Long id) {
        return serviceJpaRepository.findById(id)
                .map(serviceMapper::toDomain);
    }

    @Override
    public Optional<BookableService> findByIdForUpdate(Long id) {
        return serviceJpaRepository
                .findByIdForUpdate(id)
                .map(serviceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookableService> findAll() {
        return serviceJpaRepository.findAll()
                .stream()
                .map(serviceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<BookableService> updateActive(
            Long id,
            boolean active
    ) {
        return serviceJpaRepository.findById(id)
                .map(entity -> {
                    entity.changeActive(active);
                    return serviceMapper.toDomain(entity);
                });
    }
}
