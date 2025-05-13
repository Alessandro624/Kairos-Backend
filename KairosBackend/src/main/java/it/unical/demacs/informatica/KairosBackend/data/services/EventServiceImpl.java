package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Category;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.UserRole;
import it.unical.demacs.informatica.KairosBackend.data.repository.EventRepository;
import it.unical.demacs.informatica.KairosBackend.data.repository.StructureRepository;
import it.unical.demacs.informatica.KairosBackend.data.repository.UserRepository;
import it.unical.demacs.informatica.KairosBackend.data.repository.specifications.EventSpecifications;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventUpdateDTO;
import it.unical.demacs.informatica.KairosBackend.exception.InvalidUserRoleException;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceAlreadyExistsException;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final StructureRepository structureRepository;

    private boolean existsEvent(EventCreateDTO event) {
        return eventRepository.existsEvent(
                event.getTitle(),
                event.getDateTime(),
                event.getStructureId(),
                event.getOrganizerId()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EventDTO getEventById(UUID id) {
        Event e = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Event with id %s not found", id)));
        return modelMapper.map(e, EventDTO.class);
    }

    @Override
    @Transactional
    public void saveEvent(EventCreateDTO event) {
        if (existsEvent(event)) {
            throw new ResourceAlreadyExistsException(String.format("Event with title %s, hosted at %s %s by %s already exists", event.getTitle(),
                    event.getStructureId(), event.getDateTime(), event.getOrganizerId()));
        }
        Event newEvent = modelMapper.map(event, Event.class);
        eventRepository.save(newEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Event with id %s not found", id)));
        for (int i = 0; i < event.getImages().size(); i++) {
            // TODO: calls CDN service in order to delete images.
            log.info("Deleted image {}", event.getImages().get(i).getId());
        }
        eventRepository.delete(event);
        log.info("Event {} deleted", id);
    }

    @Override
    @Transactional
    public EventDTO updateEvent(UUID id, EventUpdateDTO eventUpdated) {
        Event event = eventRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException(String.format("Event with id %s not found", id)));
        modelMapper.map(eventUpdated, event);
        Event changedEvent = eventRepository.save(event);
        return modelMapper.map(changedEvent, EventDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventDTO> getEventsByFilter(EventSpecifications.Filter filter, Pageable pageable) {
        Specification<Event> specification = EventSpecifications.buildSpecification(filter);
        Page<Event> events = eventRepository.findAll(specification, pageable);
        return events.map(event -> modelMapper.map(event, EventDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventDTO> getAllEventsFromLocation(String city, String zip, LocalDateTime fromDate, Pageable pageable) {
        Page<Event> events = eventRepository.findEventsNearLocationStartingFrom(city, zip, fromDate, pageable);
        return events.map(e -> modelMapper.map(e, EventDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventDTO> getAllEventsByOrganizer(UUID userId, Pageable pageable) {
        User u = userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException(String.format("User with id %s not found", userId)));
        if (u.getRole() != UserRole.ORGANIZER) {
            throw new InvalidUserRoleException(String.format("User with id %s is not a organizer", userId));
        }
        Page<Event> events = eventRepository.findAllByOrganizerOrderByDateTimeAsc(u, pageable);
        return events.map(e -> modelMapper.map(e, EventDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventDTO> getAllEventsByStructure(UUID structureId, Pageable pageable) {
        Structure structure = structureRepository.findById(structureId).orElseThrow(()
                -> new ResourceNotFoundException(String.format("Structure with id %s not found", structureId)));
        Page<Event> events = eventRepository.findAllByStructureOrderByDateTimeAsc(structure, pageable);
        return events.map(e -> modelMapper.map(e, EventDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventDTO> getAllEventsByCategory(Category category, Pageable pageable) {
        Page<Event> events = eventRepository.findAllByCategoryOrderByDateTimeAsc(category, pageable);
        return events.map(e -> modelMapper.map(e, EventDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventDTO> getAllEventsBetweenTwoDates(LocalDateTime from, LocalDateTime to, Pageable pageable) {
        Page<Event> events = eventRepository.findAllByDateTimeBetweenOrderByDateTimeAsc(from, to, pageable);
        return events.map(e -> modelMapper.map(e, EventDTO.class));
    }
}
