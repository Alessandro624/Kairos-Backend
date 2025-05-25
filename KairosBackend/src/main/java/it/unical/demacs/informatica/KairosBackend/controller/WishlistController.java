package it.unical.demacs.informatica.KairosBackend.controller;

import it.unical.demacs.informatica.KairosBackend.data.services.WishlistService;
import it.unical.demacs.informatica.KairosBackend.dto.wishlist.WishlistCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.wishlist.WishlistDTO;
import it.unical.demacs.informatica.KairosBackend.dto.wishlist.WishlistFilterDTO;
import it.unical.demacs.informatica.KairosBackend.dto.wishlist.WishlistUpdateDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/v1/auth", produces = "application/json")
//TODO change
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
public class WishlistController {

    private WishlistService wishlistService;

    @GetMapping(value = "/{wishlistId}", consumes = "application/json")
    public ResponseEntity<WishlistDTO> getWishlistById(@PathVariable UUID wishlistId) {
        WishlistDTO wishlist = wishlistService.getWishlistById(wishlistId);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<WishlistDTO> createWishlist(@RequestBody WishlistCreateDTO wishlistCreateDTO) {
        WishlistDTO newWishlist = wishlistService.createWishlist(wishlistCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWishlist);
    }

    @DeleteMapping(value = "/{wishlistId}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable UUID wishlistId) {
        wishlistService.deleteWishlist(wishlistId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{wishlistId}", consumes = "application/json")
    public ResponseEntity<WishlistDTO> updateWishlist(
            @PathVariable UUID wishlistId,
            @RequestBody WishlistUpdateDTO wishlistUpdateDTO) {
        WishlistDTO updatedWishlist = wishlistService.updateWishlist(wishlistId, wishlistUpdateDTO);
        return ResponseEntity.ok(updatedWishlist);
    }

    @GetMapping(value = "/exists", consumes = "application/json")
    public ResponseEntity<Boolean> wishlistAlreadyExists(
            @RequestParam UUID creatorId,
            @RequestParam String name) {
        Boolean exists = wishlistService.wishlistAlreadyExists(creatorId, name);
        return ResponseEntity.ok(exists);
    }

    @PostMapping(value = "/{wishlistId}/users/{userId}", consumes = "application/json")
    public ResponseEntity<WishlistDTO> addUserToWishlist(
            @PathVariable UUID wishlistId,
            @PathVariable UUID userId) {
        WishlistDTO updatedWishlist = wishlistService.addUserToWishlist(wishlistId, userId);
        return ResponseEntity.ok(updatedWishlist);
    }

    @DeleteMapping(value = "/{wishlistId}/users/{userId}", consumes = "application/json")
    public ResponseEntity<Void> removeUserFromWishlist(
            @PathVariable UUID wishlistId,
            @PathVariable UUID userId) {
        wishlistService.removeUserFromWishlist(wishlistId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{wishlistId}/events/{eventId}", consumes = "application/json")
    public ResponseEntity<WishlistDTO> addEventToWishlist(
            @PathVariable UUID wishlistId,
            @PathVariable UUID eventId) {
        WishlistDTO updatedWishlist = wishlistService.addEventToWishlist(wishlistId, eventId);
        return ResponseEntity.ok(updatedWishlist);
    }

    @DeleteMapping(value = "/{wishlistId}/events/{eventId}", consumes = "application/json")
    public ResponseEntity<Void> removeEventFromWishlist(
            @PathVariable UUID wishlistId,
            @PathVariable UUID eventId) {
        wishlistService.removeEventFromWishlist(wishlistId, eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/creators/{creatorId}/count")
    public ResponseEntity<Integer> countWishlistsByCreator(@PathVariable UUID creatorId) {
        int count = wishlistService.countWishlistsByCreator(creatorId);
        return ResponseEntity.ok(count);
    }

    //IDEA: filtering rest-style
    //...?page=5&size=10
    @PostMapping("/filter")
    public ResponseEntity<Page<WishlistDTO>> getWishlists(
            @RequestBody WishlistFilterDTO wishlistFilterDTO,
            @RequestParam Integer page,
            @RequestParam Integer size) {
        Page<WishlistDTO> wishlists = wishlistService.getWishlists(wishlistFilterDTO, page, size);
        return ResponseEntity.ok(wishlists);
    }
}
