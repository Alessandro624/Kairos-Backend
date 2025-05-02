package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;

    @Override
    public Optional<Event> getEventById(UUID id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            return;
        }
        for (int i = 0; i < event.get().getImages().size(); i++) {
            // calls CDN service in order to delete images.
            log.info("Deleted image {}", event.get().getImages().get(i).getId());
        }
        eventRepository.delete(event.get());
        log.info("Event {} deleted", id);
    }

    //
    // Page<Event> getAllEventsNearLocation(Double latitude, Double longitude, Pageable pageable);
//    Page<Event> getAllEventsByOrganizer(User user, Pageable pageable);
//    Page<Event> getAllEventsByStructure(Structure structure, Pageable pageable);
//    Page<Event> getAllEventsByCategory(User user, Pageable pageable);
//    Page<Event> getAllEventsBetweenTwoDates(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
