package it.unical.demacs.informatica.KairosBackend.data.repository.specifications;

import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WishlistSpecifications {
    private WishlistSpecifications() {}

    static Specification<Wishlist> filterWishlists(WishlistFilter wishlistFilter) {
        return (root, cq, cb) ->{

            List<Predicate> predicates = new ArrayList<>();

            //Filter by wishlist name
            if(wishlistFilter.name != null)
                predicates.add(cb.like(root.get("name"), "%" + wishlistFilter.name + "%"));

            //filter by wishlist scope
            if(wishlistFilter.wishlistPropertyFilter != null){
                //join necessaria in quanto necessito dell'Id del creatore (e non di User, presente in Wishlist!)
                Join<Wishlist, User> creatorJoin = root.join("creator", JoinType.LEFT);
                Join<Wishlist, User> sharedUsersJoin = root.join("sharedUsers", JoinType.LEFT);

                Predicate owned = cb.equal(creatorJoin.get("id"), wishlistFilter.userId);
                Predicate shared = cb.equal(sharedUsersJoin.get("id"), wishlistFilter.userId);

                switch (wishlistFilter.wishlistPropertyFilter) {
                    case OWNED -> predicates.add(owned);
                    case SHARED -> predicates.add(shared);
                    case BOTH -> {
                        predicates.add(owned);
                        predicates.add(shared);
                    }
                };
            }

            //pass at least a parameter
            if (predicates.isEmpty())
                predicates.add(cb.equal(root.get("id"), -1L)); // passami almeno un filtro


            return cq.where(cb.or(predicates.toArray(new Predicate[0])))
                    .distinct(true)
                    .getRestriction();
        };
    }
}
