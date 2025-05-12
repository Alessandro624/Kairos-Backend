package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import it.unical.demacs.informatica.KairosBackend.data.entities.WishlistUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishlistUserRepository extends ListCrudRepository<WishlistUser, UUID> {
    public Optional<WishlistUser> findByUserAndWishlist(User user, Wishlist wishlist);

    public List<WishlistUser> findByWishlist(Wishlist wishlist);
    public List<WishlistUser> findByUser(User user);

    //finds all accepted shared wishlists, given a specific user
    public List<WishlistUser> findByUserAndAcceptedTrue(User user);
    //finds all pending shared wishlists, given a specific user
    public List<WishlistUser> findByUserAndAcceptedFalse(User user);

    //finds all accepted shared wishlists, given a specific wishlist
    public List<WishlistUser> findByWishlistAndAcceptedTrue(Wishlist wishlist);
    //finds all pending shared wishlists, given a specific wishlist
    public List<WishlistUser> findByWishlistAndAcceptedFalse(Wishlist wishlist);

}
