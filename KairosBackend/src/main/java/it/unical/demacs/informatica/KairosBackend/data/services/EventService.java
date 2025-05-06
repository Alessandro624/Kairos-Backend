package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Optional<Event> getEventById(UUID id);
    void saveEvent(EventCreateDTO event);
    boolean deleteEvent(UUID id);
    EventDTO updateEvent(EventDTO event);
    //
    // Page<Event> getAllEventsNearLocation(String location, Pageable pageable);
    // Page<Event> getAllEventsByOrganizer(UUID userId, Pageable pageable);
//    Page<Event> getAllEventsByStructure(Structure structure, Pageable pageable);
//    Page<Event> getAllEventsByCategory(User user, Pageable pageable);
//    Page<Event> getAllEventsBetweenTwoDates(LocalDateTime from, LocalDateTime to, Pageable pageable);

    boolean existsEvent(EventCreateDTO eventCreateDTO);
}
