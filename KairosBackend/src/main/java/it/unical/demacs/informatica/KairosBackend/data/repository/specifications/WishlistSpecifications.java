package it.unical.demacs.informatica.KairosBackend.data.repository.specifications;

import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import it.unical.demacs.informatica.KairosBackend.data.entities.WishlistUser;
import it.unical.demacs.informatica.KairosBackend.dto.wishlist.WishlistFilterDTO;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WishlistSpecifications {
    private WishlistSpecifications() {}

    public static Specification<Wishlist> filterWishlists(WishlistFilterDTO wishlistFilterDTO) {
        return (root, cq, cb) ->{

            List<Predicate> predicates = new ArrayList<>();

            //Filter by wishlist name
            if(wishlistFilterDTO.getName() != null)
                predicates.add(cb.like(root.get("name"), "%" + wishlistFilterDTO.getName() + "%"));

            //filter by wishlist scope
            if (wishlistFilterDTO.getWishlistPropertyFilter() != null) {
                //get my personal wishlists
                Predicate userOne = cb.equal(root.get("user").get("id"), wishlistFilterDTO.getUserId());

                //get shared wishlists that have been accepted
                Join<Wishlist, WishlistUser> usersJoin = root.join("users", JoinType.INNER);
                Predicate accepted = cb.isTrue(usersJoin.get("accepted"));
                Predicate userMatch = cb.equal(usersJoin.get("user").get("id"), wishlistFilterDTO.getUserId());

                //get wishlists that are owned and shared
                Predicate owned = cb.and(userOne, userMatch);
                Predicate shared = cb.and(accepted, userMatch);

                switch (wishlistFilterDTO.getWishlistPropertyFilter()) {
                    case OWNED -> predicates.add(owned);
                    case SHARED -> predicates.add(shared);
                    case BOTH -> predicates.add(cb.or(owned, shared));
                }
            }

            //pass at least a parameter
            if (predicates.isEmpty())
                predicates.add(cb.equal(root.get("id"), -1L));


            assert cq != null;
            return cq.where(cb.or(predicates.toArray(new Predicate[0])))
                    .distinct(true)
                    .getRestriction();
        };
    }
}
