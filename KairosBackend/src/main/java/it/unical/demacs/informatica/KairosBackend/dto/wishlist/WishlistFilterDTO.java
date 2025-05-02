package it.unical.demacs.informatica.KairosBackend.dto.wishlist;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class WishlistFilterDTO {
    @NotNull(message = "User id cannot be null")
    UUID userId;

    String name;
    WishlistPropertyFilter wishlistPropertyFilter;
}
