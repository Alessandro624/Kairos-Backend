package it.unical.demacs.informatica.KairosBackend.dto.wishlist;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class WishlistFilterDTO {
    @NotNull(message = "User id cannot be null")
    UUID userId;

    String name;
    WishlistPropertyFilter wishlistPropertyFilter;
}
