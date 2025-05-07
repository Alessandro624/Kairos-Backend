package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.dto.events.EventCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventUpdateDTO;

import java.util.UUID;

public interface EventService {
    EventDTO getEventById(UUID id);
    void saveEvent(EventCreateDTO event);
    void deleteEvent(UUID id);
    EventDTO updateEvent(UUID id, EventUpdateDTO eventUpdated);

    //
    // Page<Event> getAllEventsNearLocation(String location, Pageable pageable);
    // Page<Event> getAllEventsByOrganizer(UUID userId, Pageable pageable);
    // Page<Event> getAllEventsByStructure(Structure structure, Pageable pageable);
    // Page<Event> getAllEventsByCategory(User user, Pageable pageable);
    // Page<Event> getAllEventsBetweenTwoDates(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
