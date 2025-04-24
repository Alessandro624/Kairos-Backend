package it.unical.demacs.informatica.KairosBackend.dto.wishlist;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.WishlistScope;
import jakarta.validation.constraints.Size;
import lombok.Data;

//USED FOR EDITABLE FIELDS ONLY
@Data
public class EditWishlistDTO {
    @Size(min = 2, max = 50, message = "Name length must be between 2 and 50.")
    private String name;

    private WishlistScope scope;
}
