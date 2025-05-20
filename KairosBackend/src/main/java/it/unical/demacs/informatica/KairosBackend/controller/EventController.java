package it.unical.demacs.informatica.KairosBackend.controller;

import it.unical.demacs.informatica.KairosBackend.data.services.EventService;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping(path = "/v1/events", produces = "application/json")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Page<EventDTO>> getAllEvents(@RequestParam Pageable pageable) {
        return ResponseEntity.ok(eventService.getAllEventsGeneral(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable UUID id) {
        EventDTO event = eventService.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(event);
    }

//    @PostMapping
//    public ResponseEntity<EventDTO> createEvent(@RequestBody @Valid EventCreateDTO eventSubmitted) {
//        // TODO: check
//        //eventService.saveEvent(eventSubmitted);
//    }
}
