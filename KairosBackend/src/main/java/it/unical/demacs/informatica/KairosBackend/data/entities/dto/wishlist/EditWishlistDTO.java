package it.unical.demacs.informatica.KairosBackend.data.entities.dto.wishlist;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.WishlistScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

//USED FOR EDITABLE FIELDS ONLY
@Data
public class EditWishlistDTO {
    @Size(min = 2, max = 50, message = "Name length must be between 2 and 50.")
    @NotBlank(message = "Name must be defined")
    private String name;

    @NotNull(message = "Scope must be defined")
    private WishlistScope scope;
}
