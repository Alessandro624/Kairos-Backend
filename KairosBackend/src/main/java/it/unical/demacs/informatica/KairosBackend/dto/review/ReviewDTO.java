package it.unical.demacs.informatica.KairosBackend.dto.review;

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

    @NotBlank(message = "Event cannot be null")
    private String eventParticipated; // eventually waiting for EventDTO

    @NotNull(message = "Rating cannot be blank")
    private int rating;

    @NotBlank(message = "Comment cannot be blank")
    private String comment;
}
