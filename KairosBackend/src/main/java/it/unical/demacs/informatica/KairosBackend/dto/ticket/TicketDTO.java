package it.unical.demacs.informatica.KairosBackend.dto.ticket;

import it.unical.demacs.informatica.KairosBackend.dto.user.UserProfileDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {

    @NotNull(message = "Id must be defined")
    private UUID id;

    @NotNull(message = "Participant cannot be null")
    private UserProfileDTO participant;

    @NotBlank(message = "Event cannot be blank")
    private String event; // eventually waiting for EventDTO

    @NotBlank(message = "Sector cannot be blank")
    private String sector; // eventually waiting for SectorDTO

    @NotNull(message = "Issue date cannot be null")
    private LocalDate issueDate;

}
