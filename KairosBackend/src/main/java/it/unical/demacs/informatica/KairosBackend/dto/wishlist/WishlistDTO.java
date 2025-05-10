package it.unical.demacs.informatica.KairosBackend.dto.wishlist;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.WishlistScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

//MAIN DTO RETURNED TO FRONTEND
@Data
@NoArgsConstructor
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
    private UUID creator;

    private List<UUID> wishedEvents;

    private List<UUID> sharedUsers;

    /*used mainly for testing (maybe)
    public void addWishedEvent(EventWishlistDTO e){
        wishedEvents.add(e);
    }

    public void addSharedUser(UserWishlistDTO u){
        sharedUsers.add(u);
    }
     */
}
