package it.unical.demacs.informatica.KairosBackend.dto.events;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Category;
import it.unical.demacs.informatica.KairosBackend.dto.events.eventimage.EventImageCreateDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventUpdateDTO {

    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters.")
    private String title;

    @Size(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters.")
    private String description;

    private Category category;

    @Future(message = "dateTime must be in the future.")
    private LocalDateTime dateTime;

    @Positive(message = "maxParticipants must be positive.")
    private int maxParticipants;

    private UUID structureId;

    // TODO: EventSectorDTO is missing
    // private List<EventSectorDTO> sectors;

    // can this be null ?
    private List<EventImageCreateDTO> images;

}
