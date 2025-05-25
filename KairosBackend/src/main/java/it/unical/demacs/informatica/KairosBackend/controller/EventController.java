package it.unical.demacs.informatica.KairosBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.unical.demacs.informatica.KairosBackend.data.repository.specifications.EventSpecifications;
import it.unical.demacs.informatica.KairosBackend.data.services.EventService;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventUpdateDTO;
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

    // ALL
    @Operation(summary = "Get all events", description = "Retrieve a list of all events.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<EventDTO>> getAllEvents(@RequestParam Pageable pageable, @ModelAttribute @RequestParam(required = false) EventSpecifications.Filter filter) {
        return ResponseEntity.ok(eventService.getEventsByFilter(filter, pageable));
    }

    @Operation(summary = "Get an event by ID", description = "Retrieve detailed information about a specific event, including name, date, location, description, and associated details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid event ID supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable UUID id) {
        EventDTO event = eventService.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(event);
    }

    @Operation(summary = "Create a new event", description = "Allows an authorized user to create a new event by providing details such as name, date, location, and description.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody @Valid EventCreateDTO eventSubmitted) {
        // check if the user that is making the request, is an organizer
        return ResponseEntity.ok(eventService.saveEvent(eventSubmitted));
    }

    @Operation(summary = "Update an event", description = "Update an existing event by ID. Only authorized users can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or event ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable UUID id, @RequestBody @Valid EventUpdateDTO eventUpdated) {
        // SAME AUTHORIZATION LOGIC HERE
        return ResponseEntity.ok(eventService.updateEvent(id, eventUpdated));
    }

    @Operation(summary = "Delete an event", description = "Delete an existing event by its ID. Only authorized users can delete events.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid event ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        // AUTHORIZATION LOGIC
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}