package it.unical.demacs.informatica.KairosBackend;

import it.unical.demacs.informatica.KairosBackend.data.services.UserService;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserUpdateDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class UserServiceTest {
    @Value("classpath:data/users.csv")
    private Resource users;

    private final UserService userService;

    private static UUID testUserId;
    private static UUID adminUserId;

    private static boolean isInitialized = false;

    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    public void setupTestData() throws IOException {
        if (!isInitialized) {
            CSVParser userCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("username", "email", "firstName", "lastName", "password", "phoneNumber", "role", "provider", "emailVerified")
                    .withSkipHeaderRecord(true)
                    .parse(new InputStreamReader(users.getInputStream()));

            for (CSVRecord record : userCsv) {
                UserCreateDTO user = insertUserInDB(
                        record.get("username"),
                        record.get("email"),
                        record.get("firstName"),
                        record.get("lastName"),
                        record.get("password"),
                        record.get("phoneNumber")
                );

                if (testUserId == null && record.get("role").equals("USER")) {
                    Optional<UserDTO> createdUser = userService.findByUsername(user.getUsername());
                    testUserId = createdUser.map(UserDTO::getId).orElse(null);
                }

                if (adminUserId == null && record.get("role").equals("ADMIN")) {
                    Optional<UserDTO> createdUser = userService.findByUsername(user.getUsername());
                    adminUserId = createdUser.map(UserDTO::getId).orElse(null);
                }
            }

            isInitialized = true;
        }
    }

    private UserCreateDTO insertUserInDB(
            String username,
            String email,
            String firstName,
            String lastName,
            String password,
            String phoneNumber
    ) {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername(username);
        userCreateDTO.setEmail(email);
        userCreateDTO.setFirstName(firstName);
        userCreateDTO.setLastName(lastName);
        userCreateDTO.setPassword(password);
        userCreateDTO.setPhoneNumber(phoneNumber);

        userService.createUser(userCreateDTO);
        return userCreateDTO;
    }

    @Test
    public void testFindById() {
        assertNotNull(testUserId, "Test user ID should not be null");

        Optional<UserDTO> user = userService.findById(testUserId);

        assertTrue(user.isPresent(), "User should be found by ID");
        assertEquals(testUserId, user.get().getId(), "User ID should match");
    }

    @Test
    public void testFindByUsername() {
        String username = "testuser";

        Optional<UserDTO> user = userService.findByUsername(username);

        assertTrue(user.isPresent(), "User should be found by username");
        assertEquals(username, user.get().getUsername(), "Username should match");
    }

    @Test
    public void testFindByEmail() {
        String email = "test@example.com";

        Optional<UserDTO> user = userService.findByEmail(email);

        assertTrue(user.isPresent(), "User should be found by email");
        assertEquals(email, user.get().getEmail(), "Email should match");
    }

    @Test
    public void testCreateUser() {
        UserCreateDTO newUser = new UserCreateDTO();
        newUser.setUsername("newuser");
        newUser.setEmail("newuser@example.com");
        newUser.setFirstName("New");
        newUser.setLastName("User");
        newUser.setPassword("password123");
        newUser.setPhoneNumber("+123456789");

        UserDTO createdUser = userService.createUser(newUser);

        assertNotNull(createdUser.getId(), "Created user should have an ID");
        assertEquals(newUser.getUsername(), createdUser.getUsername(), "Username should match");
        assertEquals(newUser.getEmail(), createdUser.getEmail(), "Email should match");
        assertEquals(newUser.getFirstName(), createdUser.getFirstName(), "First name should match");
        assertEquals(newUser.getLastName(), createdUser.getLastName(), "Last name should match");
        assertEquals(newUser.getPhoneNumber(), createdUser.getPhoneNumber(), "Phone number should match");
    }

    @Test
    public void testCreateDuplicateUsernameFails() {
        UserCreateDTO newUser = new UserCreateDTO();
        newUser.setUsername("testuser");
        newUser.setEmail("unique@example.com");
        newUser.setFirstName("Unique");
        newUser.setLastName("User");
        newUser.setPassword("password123");

        assertThrows(Exception.class, () -> userService.createUser(newUser), "Creating user with duplicate username should fail");
    }

    @Test
    public void testCreateDuplicateEmailFails() {
        UserCreateDTO newUser = new UserCreateDTO();
        newUser.setUsername("uniqueuser");
        newUser.setEmail("test@example.com");
        newUser.setFirstName("Unique");
        newUser.setLastName("User");
        newUser.setPassword("password123");

        assertThrows(Exception.class, () -> userService.createUser(newUser), "Creating user with duplicate email should fail");
    }

    @Test
    public void testUpdateUser() {
        assertNotNull(testUserId, "Test user ID should not be null");

        UserUpdateDTO updateData = new UserUpdateDTO();
        updateData.setFirstName("UpdatedFirstName");
        updateData.setLastName("UpdatedLastName");
        updateData.setPhoneNumber("+987654321");

        UserDTO updatedUser = userService.updateUser(testUserId, updateData);

        assertEquals(testUserId, updatedUser.getId(), "User ID should match");
        assertEquals("UpdatedFirstName", updatedUser.getFirstName(), "First name should be updated");
        assertEquals("UpdatedLastName", updatedUser.getLastName(), "Last name should be updated");
        assertEquals("+987654321", updatedUser.getPhoneNumber(), "Phone number should be updated");
    }

    @Test
    public void testFindAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserDTO> users = userService.findAllUsers(pageable);

        assertNotNull(users, "Result should not be null");
        assertTrue(users.getTotalElements() > 0, "At least one user should be returned");
    }

    @Test
    public void testFindAllUsersAdmin() {
        /* TODO Test after better handling of ROLE
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserDTO> users = userService.findAllUsersAdmin(pageable);

        assertNotNull(users, "Result should not be null");
        assertTrue(users.getTotalElements() > 0, "At least one user should be returned");

        boolean foundAdmin = users.getContent().stream().allMatch(user -> user.getRole() == UserRole.ADMIN);
        assertTrue(foundAdmin, "findAllUsersAdmin gives all admins");
        */
    }

    @Test
    public void testExistsUsername() {
        String username = "testuser";
        boolean exists = userService.existsUsername(username);
        assertTrue(exists, "Username should exist");

        String nonExistingUsername = "nonexistinguser";
        boolean notExists = userService.existsUsername(nonExistingUsername);
        assertFalse(notExists, "Username should not exist");
    }

    @Test
    public void testExistsEmail() {
        String email = "test@example.com";
        boolean exists = userService.existsEmail(email);
        assertTrue(exists, "Email should exist");

        String nonExistingEmail = "nonexisting@example.com";
        boolean notExists = userService.existsEmail(nonExistingEmail);
        assertFalse(notExists, "Email should not exist");
    }

    @Test
    public void testDeleteUser() {
        UserCreateDTO userToDelete = new UserCreateDTO();
        userToDelete.setUsername("userToDelete");
        userToDelete.setEmail("userToDelete@example.com");
        userToDelete.setFirstName("Delete");
        userToDelete.setLastName("User");
        userToDelete.setPassword("password123");

        UserDTO createdUser = userService.createUser(userToDelete);
        UUID userId = createdUser.getId();
        userService.deleteUser(userId);

        Optional<UserDTO> deletedUser = userService.findById(userId);
        assertFalse(deletedUser.isPresent(), "User should be deleted");
    }

    @Test
    public void testNonExistingId() {
        UUID nonExistingId = UUID.randomUUID();

        Optional<UserDTO> user = userService.findById(nonExistingId);

        assertFalse(user.isPresent(), "No user should be found for non-existing ID");
    }
}
