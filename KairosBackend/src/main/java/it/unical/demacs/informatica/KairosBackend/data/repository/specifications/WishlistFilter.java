package it.unical.demacs.informatica.KairosBackend.data.repository.specifications;

import java.util.UUID;

//FIXME: is this a DTO? (i think it isn't)
public class WishlistFilter {
    UUID userId;
    String name;
    WishlistPropertyFilter wishlistPropertyFilter;
}
