package it.unical.demacs.informatica.KairosBackend.data.entities.dto.wishlist;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.WishlistScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

//MAIN DTO RETURNED TO FRONTEND
@Data
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

    @NotNull(message = "Creator must be defined")
    private UserWishlistDTO creator;

    private List<EventWishlistDTO> wishedEvents;

    private List<UserWishlistDTO> sharedUsers;
}
