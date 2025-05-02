package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Optional<Event> getEventById(UUID id);
    Event saveEvent(Event event);
    void deleteEvent(UUID id);

    //
    // Page<Event> getAllEventsNearLocation(Double latitude, Double longitude, Pageable pageable);
//    Page<Event> getAllEventsByOrganizer(User user, Pageable pageable);
//    Page<Event> getAllEventsByStructure(Structure structure, Pageable pageable);
//    Page<Event> getAllEventsByCategory(User user, Pageable pageable);
//    Page<Event> getAllEventsBetweenTwoDates(LocalDateTime from, LocalDateTime to, Pageable pageable);

}
