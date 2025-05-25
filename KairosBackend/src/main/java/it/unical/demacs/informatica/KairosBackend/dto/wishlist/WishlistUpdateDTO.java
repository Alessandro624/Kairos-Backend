package it.unical.demacs.informatica.KairosBackend.dto.wishlist;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.WishlistScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

//USED FOR EDITABLE FIELDS ONLY
@Data
public class WishlistUpdateDTO {
    @Size(min = 2, max = 50, message = "Name length must be between 2 and 50.")
    private String name;

    private WishlistScope scope;

    //FIXME maybe it's not necessary.
    private List<UUID> wishedEvents;

    private List<UUID> sharedUsers;
}
