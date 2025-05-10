package it.unical.demacs.informatica.KairosBackend.dto.review;

import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateDTO {

    @NotNull(message = "Event cannot be null")
    private EventDTO eventParticipated;

    @Size(min = 1, max = 5, message = "Rating must be between 1 and 5")
    @NotNull(message = "Rating cannot be blank")
    private int rating;

    @Size(min = 2, max = 500, message = "Used not enough or too much characters for the comment")
    @NotBlank(message = "Comment cannot be blank")
    private String comment;

}
