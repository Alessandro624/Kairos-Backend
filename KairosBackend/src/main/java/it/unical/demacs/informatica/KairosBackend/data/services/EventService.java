package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Category;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EventService {
    EventDTO getEventById(UUID id);
    void saveEvent(EventCreateDTO event);
    void deleteEvent(UUID id);
    EventDTO updateEvent(UUID id, EventUpdateDTO eventUpdated);

    Page<EventDTO> getAllEventsFromLocation(String city, String zip, LocalDateTime fromDate, Pageable pageable);
    Page<EventDTO> getAllEventsByOrganizer(UUID userId, Pageable pageable);
    Page<EventDTO> getAllEventsByStructure(Structure structure, Pageable pageable);
    Page<EventDTO> getAllEventsByCategory(Category Category, Pageable pageable);
    Page<EventDTO> getAllEventsBetweenTwoDates(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
