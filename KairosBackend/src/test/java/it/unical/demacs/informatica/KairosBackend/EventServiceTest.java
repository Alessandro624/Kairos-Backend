package it.unical.demacs.informatica.KairosBackend;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.repository.EventRepository;
import it.unical.demacs.informatica.KairosBackend.data.repository.StructureRepository;
import it.unical.demacs.informatica.KairosBackend.data.repository.UserRepository;
import it.unical.demacs.informatica.KairosBackend.data.services.EventServiceImpl;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StructureRepository structureRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private EventCreateDTO eventCreateDTO;
    private Event event;
    private EventDTO eventDTO;
    private UUID eventId;

    @BeforeEach
    public void setUp() {
        eventId = UUID.randomUUID();
        eventCreateDTO = new EventCreateDTO();
        eventCreateDTO.setTitle("Test Event");
        eventCreateDTO.setDateTime(LocalDateTime.now());
        eventCreateDTO.setStructureId(UUID.randomUUID());
        eventCreateDTO.setOrganizerId(UUID.randomUUID());

        event = new Event();
        event.setId(eventId);
        eventDTO = new EventDTO();
        eventDTO.setId(eventId);
    }

    @Test
    public void testGetEventById() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(modelMapper.map(event, EventDTO.class)).thenReturn(eventDTO);

        EventDTO result = eventService.getEventById(eventId);

        assertEquals(eventDTO, result);
        verify(eventRepository).findById(eventId);
    }

    @Test
    public void testSaveEvent() {
        when(eventRepository.existsEvent(anyString(), any(), any(), any())).thenReturn(false);
        when(modelMapper.map(eventCreateDTO, Event.class)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);

        eventService.saveEvent(eventCreateDTO);

        verify(eventRepository).existsEvent(anyString(), any(), any(), any());
        verify(eventRepository).save(event);
    }

    @Test
    public void testDeleteEvent() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        eventService.deleteEvent(eventId);

        verify(eventRepository).delete(event);
    }
}
