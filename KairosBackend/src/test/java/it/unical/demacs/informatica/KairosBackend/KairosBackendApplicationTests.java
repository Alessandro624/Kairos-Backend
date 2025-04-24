package it.unical.demacs.informatica.KairosBackend;

import it.unical.demacs.informatica.KairosBackend.dto.wishlist.EventWishlistDTO;
import it.unical.demacs.informatica.KairosBackend.dto.wishlist.UserWishlistDTO;
import it.unical.demacs.informatica.KairosBackend.dto.wishlist.WishlistDTO;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.WishlistScope;
import it.unical.demacs.informatica.KairosBackend.data.services.WishlistService;
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

    //package visibility:
    WishlistService wishlistService;

    //TODO change strategy. The constructor will be too long
    public KairosBackendApplicationTests(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    private static boolean isInitialized = false;

    //we use BeforeEach and not BeforeAll because of dependency injection
    //createDbTest should, in fact, be static, so as wishlistRepository.
    //is there a way to apply dependency injection to static fields?
    //and primarily, does it make sense?
    @BeforeEach
    public void createDbTest() throws IOException {
        if (!isInitialized) {
            CSVParser wishlistCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .parse(new InputStreamReader(wishlists.getInputStream()));

            for (CSVRecord record : wishlistCsv) {
                //FIXME use reflection, a layer supertype(maybe?) and a single method 'insertData' for better code (no repetition)
                insertWishlistInDB(record.get(0), record.get(1), record.get(2), record.get(3), record.get(4), record.get(5));
            }
            isInitialized = true;
        }
    }

    private void insertWishlistInDB(
            String name,
            String creationDate,
            String scope,
            String creator,
            String wishedEvents,
            String sharedUsers
    ) {
        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setName(name);
        wishlistDTO.setCreationDate(LocalDate.parse(creationDate));
        wishlistDTO.setScope(WishlistScope.valueOf(scope));

        //creator
        UserWishlistDTO creatorDTO = new UserWishlistDTO();
        creatorDTO.setUsername(creator);
        wishlistDTO.setCreator(creatorDTO);

        //wished events
        String[] wishedEventsArray = wishedEvents.split("-");
        for (String event : wishedEventsArray) {
            EventWishlistDTO eventWishlistDTO = new EventWishlistDTO();
            eventWishlistDTO.setName(event);
            wishlistDTO.addWishedEvent(eventWishlistDTO);
        }

        String[] sharedUsersArray = sharedUsers.split("-");
        for (String user : sharedUsersArray) {
            UserWishlistDTO userWishlistDTO = new UserWishlistDTO();
            userWishlistDTO.setUsername(user);
            wishlistDTO.addSharedUser(userWishlistDTO);
        }

        wishlistService.saveWishlist(wishlistDTO);
    }
}
