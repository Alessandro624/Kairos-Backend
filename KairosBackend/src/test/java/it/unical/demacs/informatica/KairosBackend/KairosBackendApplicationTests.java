package it.unical.demacs.informatica.KairosBackend;

import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import it.unical.demacs.informatica.KairosBackend.repository.WishlistRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class KairosBackendApplicationTests {

	@Value("classpath:data/wishlists.csv")
	private Resource wishlists;

	//package visibility:
	WishlistRepository wishlistRepository;

	public KairosBackendApplicationTests(WishlistRepository wishlistRepository) {
		this.wishlistRepository = wishlistRepository;
	}
}
