package it.unical.demacs.informatica.KairosBackend.data.entities.dto.wishlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

//EVENT DATA USED IN WishlistDTO class
@Data
public class EventWishlistDTO {
    @Size(min = 2, max = 50, message = "Event name length must be between 2 and 50.")
    @NotBlank(message = "Event name must be defined")
    private String name;

    //TODO add event image
    //private EventImage eventImage
}
