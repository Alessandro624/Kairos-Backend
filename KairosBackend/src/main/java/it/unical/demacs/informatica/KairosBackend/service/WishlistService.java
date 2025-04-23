package it.unical.demacs.informatica.KairosBackend.service;

import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import it.unical.demacs.informatica.KairosBackend.data.entities.dto.wishlist.EditWishlistDTO;
import it.unical.demacs.informatica.KairosBackend.data.entities.dto.wishlist.EventWishlistDTO;
import it.unical.demacs.informatica.KairosBackend.data.entities.dto.wishlist.UserWishlistDTO;
import it.unical.demacs.informatica.KairosBackend.data.entities.dto.wishlist.WishlistDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishlistService {
    void saveWishlist(WishlistDTO wishlistDTO);
    void deleteWishlist(UUID wishlistId);
    void updateWishlist(EditWishlistDTO editWishlistDTO);

    void addUserToWishlist(UUID wishlistId, UserWishlistDTO userWishlistDTO);
    void removeUserFromWishlist(UUID wishlistId, UUID userId);

    void addEventToWishlist(UUID wishlistId, EventWishlistDTO eventId);
    void removeEventFromWishlist(UUID wishlistId, UUID userId);

    int countWishlistsByCreator(UUID creatorId);

    //TODO improve methods: a single method with a filter (private, shared, both) should be better
    //get created wishlists
    Page<WishlistDTO> getCreatorWishlists(UUID creatorId, Integer page, Integer size);
    Page<WishlistDTO> getCreatorWishlists(String pattern, UUID creatorId, Integer page, Integer size);

    //intended as "wishlists shared from others"
    Page<WishlistDTO> getSharedWishlists(UUID creatorId, Integer page, Integer size);

    //get all wishlists: shared and private
    Page<WishlistDTO> getWishlists(UUID creatorId, Integer page, Integer size);
}
