package it.unical.demacs.informatica.KairosBackend.dto.review;

import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    @NotNull(message = "Id must be defined")
    private UUID id;

    @NotNull(message = "Event cannot be null")
    private EventDTO eventParticipated;

    @NotNull(message = "Rating cannot be blank")
    private int rating;

    @NotBlank(message = "Comment cannot be blank")
    private String comment;
}
