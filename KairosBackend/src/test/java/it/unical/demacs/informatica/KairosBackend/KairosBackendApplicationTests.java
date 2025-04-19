package it.unical.demacs.informatica.KairosBackend;

import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import it.unical.demacs.informatica.KairosBackend.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class KairosBackendApplicationTests {

	@Value("classpath:data/wishlists.csv")
	private Resource wishlists;

	//FIXME use directly with services
	//package visibility:
	WishlistRepository wishlistRepository;

	public KairosBackendApplicationTests(WishlistRepository wishlistRepository) {
		this.wishlistRepository = wishlistRepository;
	}

	private static boolean isInitialized = false;

	//we use BeforeEach and not BeforeAll because of dependency injection
	//createDbTest should, in fact, be static, so as wishlistRepository.
	//is there a way to apply dependency injection to static fields?
	//and primarily, does it make sense?
	@BeforeEach
	public void createDbTest() throws IOException {
		if(!isInitialized) {
			CSVParser wishlistCsv = CSVFormat.DEFAULT.withDelimiter(';')
					.parse(new InputStreamReader(wishlists.getInputStream()));

			for(CSVRecord record : wishlistCsv) {
				//FIXME use reflection, a layer supertype(maybe?) and a single method 'insertData' for better code (no repetition)
				insertWishlist(record.get(0), record.get(1), record.get(2), record.get(3), record.get(4), record.get(5));
			}
			isInitialized = true;
		}
	}

	private static void insertWishlist(
			String name,
			String creationDate,
			String scope,
			String creator,
			String wishedEvents,
			String sharedUsers
	) {
		Wishlist wishlist = new Wishlist();
		wishlist.setName(name);
		wishlist.setCreationDate(LocalDate.parse(creationDate));
		//TODO add creator
		//wishlist.setCreator(userRepository.findById(UUID.fromString(creator)));

		//TODO add n-m relations
		/*
		WISHED EVENTS

		String[] wishedEventsArray = wishedEvents.split("-");
        for (String event: wishedEventsArray) {
      		Event event = eventRepository.findById(UUID.fromString(event)));
      		wishlist.addWishedEvent(event);
    	}
		*/

		/*
		SHARED USERS

		String[] sharedUsersArray = sharedUsers.split("-");
        for (String user: sharedUsersArray) {
      		User user = userRepository.findById(UUID.fromString(user)));
			wishlist.addSharedUser(user).
    	}
		*/

		//wishlistRepository.save(wishlist)
	}
}
