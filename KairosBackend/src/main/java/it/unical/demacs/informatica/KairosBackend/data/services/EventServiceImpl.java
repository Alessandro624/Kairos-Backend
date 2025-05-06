package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.repository.EventRepository;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceAlreadyExistsException;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    @Override
    public Optional<Event> getEventById(UUID id) {
        return eventRepository.findById(id);
    }

    @Override
    public void saveEvent(EventCreateDTO event) {
        if (existsEvent(event)) {
            throw new ResourceAlreadyExistsException(String.format("Event with title %s, hosted at %s %s by %s already exists", event.getTitle(),
                    event.getStructureId(), event.getDateTime(), event.getOrganizerId()));
        }
        Event newEvent = modelMapper.map(event, Event.class);
        // TODO: how do I test this??
        eventRepository.save(newEvent);
    }

    @Override
    @Transactional
    public boolean deleteEvent(UUID id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Event with id %s not found", id));
        }
        for (int i = 0; i < event.get().getImages().size(); i++) {
            // calls CDN service in order to delete images.
            log.info("Deleted image {}", event.get().getImages().get(i).getId());
        }
        eventRepository.delete(event.get());
        log.info("Event {} deleted", id);
        return true;
    }

    @Override
    public EventDTO updateEvent () {
        return new EventDTO();
    }

    @Override
    public boolean existsEvent(EventCreateDTO event) {
        return eventRepository.existsEvent(
                event.getTitle(),
                event.getDateTime(),
                event.getStructureId(),
                event.getOrganizerId()
        );
    }

    //
    // Page<Event> getAllEventsNearLocation(String location, Pageable pageable);
//    Page<Event> getAllEventsByOrganizer(User user, Pageable pageable);
//    Page<Event> getAllEventsByStructure(Structure structure, Pageable pageable);
//    Page<Event> getAllEventsByCategory(User user, Pageable pageable);
//    Page<Event> getAllEventsBetweenTwoDates(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
