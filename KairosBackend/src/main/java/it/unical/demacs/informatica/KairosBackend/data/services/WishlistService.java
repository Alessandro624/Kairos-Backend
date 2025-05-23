package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.dto.wishlist.WishlistFilterDTO;
import it.unical.demacs.informatica.KairosBackend.dto.wishlist.*;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface WishlistService {
    WishlistDTO getWishlistById(UUID id);

    WishlistDTO createWishlist(WishlistCreateDTO wishlistCreateDTO);
    void deleteWishlist(UUID wishlistId);
    WishlistDTO updateWishlist(UUID wishlistID, WishlistUpdateDTO wishlistUpdateDTO);

    Boolean wishlistAlreadyExists(UUID creatorId, String name);

    WishlistDTO addUserToWishlist(UUID wishlistId, UUID userId);
    //TODO acceptWishlist method: the user will accept the pending wishlist invite.
    void removeUserFromWishlist(UUID wishlistId, UUID userId);

    WishlistDTO addEventToWishlist(UUID wishlistId, UUID eventId);
    void removeEventFromWishlist(UUID wishlistId, UUID eventId);

    int countWishlistsByCreator(UUID creatorId);

    /*

    //get created wishlists
    Page<WishlistDTO> getCreatorWishlists(UUID creatorId, Integer page, Integer size);
    Page<WishlistDTO> getCreatorWishlists(String pattern, UUID creatorId, Integer page, Integer size);
    //intended as "wishlists shared from others"
    Page<WishlistDTO> getSharedWishlists(UUID creatorId, Integer page, Integer size);
    //get all wishlists: shared and private
    Page<WishlistDTO> getWishlists(UUID creatorId, Integer page, Integer size);

    */

    //Single filtering method for all the stuff above.
    Page<WishlistDTO> getWishlists(WishlistFilterDTO wishlistFilterDTO, Integer page, Integer size);
}
