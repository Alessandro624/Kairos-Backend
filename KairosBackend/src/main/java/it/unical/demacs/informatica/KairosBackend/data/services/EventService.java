package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Category;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import it.unical.demacs.informatica.KairosBackend.data.repository.specifications.EventSpecifications;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EventService {
    EventDTO getEventById(UUID id);
    void saveEvent(EventCreateDTO event);
    void deleteEvent(UUID id);
    EventDTO updateEvent(UUID id, EventUpdateDTO eventUpdated);

    Page<EventDTO> getEventsByFilter(EventSpecifications.Filter filter, Pageable pageable);
    Page<EventDTO> getAllEventsFromLocation(String city, String zip, LocalDateTime fromDate, Pageable pageable);
    Page<EventDTO> getAllEventsByOrganizer(UUID userId, Pageable pageable);
    Page<EventDTO> getAllEventsByStructure(UUID structureId, Pageable pageable);
    Page<EventDTO> getAllEventsByCategory(Category Category, Pageable pageable);
    Page<EventDTO> getAllEventsBetweenTwoDates(LocalDateTime from, LocalDateTime to, Pageable pageable);
}