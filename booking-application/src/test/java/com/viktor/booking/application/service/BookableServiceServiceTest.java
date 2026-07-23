package com.viktor.booking.application.service;

import com.viktor.booking.application.repository.BookableServiceRepository;
import com.viktor.booking.domain.model.BookableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookableServiceServiceTest {

    @Mock
    private BookableServiceRepository serviceRepository;

    @InjectMocks
    private BookableServiceService serviceService;

    @Test
    void shouldCreateActiveService() {
        when(serviceRepository.save(any(BookableService.class)))
                .thenAnswer(invocation -> {
                    BookableService service =
                            invocation.getArgument(0);

                    return new BookableService(
                            1L,
                            service.getName(),
                            service.getDescription(),
                            service.getDurationMinutes(),
                            service.isActive()
                    );
                });

        BookableService createdService =
                serviceService.createService(
                        "Java consultation",
                        "Individual Java consultation",
                        60
                );

        ArgumentCaptor<BookableService> serviceCaptor =
                ArgumentCaptor.forClass(BookableService.class);

        verify(serviceRepository)
                .save(serviceCaptor.capture());

        BookableService servicePassedToRepository =
                serviceCaptor.getValue();

        assertThat(servicePassedToRepository.getId())
                .isNull();

        assertThat(servicePassedToRepository.getName())
                .isEqualTo("Java consultation");

        assertThat(servicePassedToRepository.getDescription())
                .isEqualTo("Individual Java consultation");

        assertThat(servicePassedToRepository.getDurationMinutes())
                .isEqualTo(60);

        assertThat(servicePassedToRepository.isActive())
                .isTrue();

        assertThat(createdService.getId())
                .isEqualTo(1L);
    }

    @Test
    void shouldReturnAllServices() {
        List<BookableService> services = List.of(
                new BookableService(
                        1L,
                        "Java consultation",
                        "Java",
                        60,
                        true
                ),
                new BookableService(
                        2L,
                        "Docker consultation",
                        "Docker",
                        45,
                        false
                )
        );

        when(serviceRepository.findAll())
                .thenReturn(services);

        List<BookableService> result =
                serviceService.getAllServices();

        assertThat(result)
                .containsExactlyElementsOf(services);

        verify(serviceRepository)
                .findAll();
    }

    @Test
    void shouldReturnServiceById() {
        BookableService service = new BookableService(
                1L,
                "Spring consultation",
                "Spring",
                60,
                true
        );

        when(serviceRepository.findById(1L))
                .thenReturn(Optional.of(service));

        Optional<BookableService> result =
                serviceService.getServiceById(1L);

        assertThat(result)
                .containsSame(service);

        verify(serviceRepository)
                .findById(1L);
    }

    @Test
    void shouldActivateServiceById() {
        BookableService activatedService =
                new BookableService(
                        1L,
                        "Backend consultation",
                        "Backend",
                        60,
                        true
                );

        when(serviceRepository.updateActive(1L, true))
                .thenReturn(Optional.of(activatedService));

        Optional<BookableService> result =
                serviceService.activateServiceById(1L);

        assertThat(result)
                .containsSame(activatedService);

        verify(serviceRepository)
                .updateActive(1L, true);
    }

    @Test
    void shouldDeactivateServiceById() {
        BookableService deactivatedService =
                new BookableService(
                        1L,
                        "Backend consultation",
                        "Backend",
                        60,
                        false
                );

        when(serviceRepository.updateActive(1L, false))
                .thenReturn(Optional.of(deactivatedService));

        Optional<BookableService> result =
                serviceService.deactivateServiceById(1L);

        assertThat(result)
                .containsSame(deactivatedService);

        verify(serviceRepository)
                .updateActive(1L, false);
    }
}