package it.unical.demacs.informatica.KairosBackend;

import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import it.unical.demacs.informatica.KairosBackend.repository.WishlistRepository;
import org.junit.Test;

import java.util.List;

public class WishlistTest extends KairosBackendApplicationTests {

    public WishlistTest(WishlistRepository wishlistRepository) {
        super(wishlistRepository);
    }
}
