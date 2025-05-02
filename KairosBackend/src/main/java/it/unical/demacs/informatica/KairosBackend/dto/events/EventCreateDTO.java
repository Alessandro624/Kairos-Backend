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
public class EventCreateDTO {

    @NotBlank(message = "title cannot be blank.")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters.")
    private String title;

    @NotBlank(message = "description cannot be blank")
    @Size(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters.")
    private String description;

    @NotNull(message = "category cannot be null.")
    private Category category;

    @Future(message = "dateTime must be in the future.")
    private LocalDateTime dateTime;

    @Positive(message = "maxParticipants must be positive.")
    @NotNull(message = "maxParticipants cannot be null.")
    private int maxParticipants;
    
    @NotNull(message = "organizerId cannot be null.")
    private UUID organizerId;

    @NotNull(message = "structureId cannot be null.")
    private UUID structureId;

    // DTO of these two entities is missing...

    private List<EventSector> sectors;

    private List<EventImageCreateDTO> images;
}
