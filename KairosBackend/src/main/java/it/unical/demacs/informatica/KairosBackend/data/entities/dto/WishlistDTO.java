package it.unical.demacs.informatica.KairosBackend.data.entities.dto;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.WishlistScope;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class WishlistDTO {

    @NotNull(message = "Id must be defined")
    private UUID id;

    @Size(min = 2, max = 50, message = "Name length must be between 2 and 50.")
    @NotBlank(message = "Name must be defined")
    private String name;

    @NotNull(message = "Creation date must be defined")
    private LocalDate creationDate;

    @NotNull(message = "Scope must be defined")
    private WishlistScope scope;

    //TODO add UserDTO
    //@NotNull(message = "Creator must be defined")
    //private UserDTO creator;

    //TODO add EventDTO
    //private List<EventDTO> wishedEvents;

    //TODO add UserDTO
    //private List<UserDTO> sharedUsers;

}
