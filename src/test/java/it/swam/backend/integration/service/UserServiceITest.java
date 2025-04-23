package it.swam.backend.integration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import it.swam.backend.common.MongoDbTestConfig;
import it.swam.backend.dto.UserCreateDto;
import it.swam.backend.dto.UserCreateResponseDto;
import it.swam.backend.dto.UserModifyDto;
import it.swam.backend.dto.UserResponseDto;
import it.swam.backend.entity.User;
import it.swam.backend.exception.NotFoundException;
import it.swam.backend.repository.UserRepository;
import it.swam.backend.service.UserService;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserServiceITest extends MongoDbTestConfig {

    @Autowired
    private UserService service;

    @MockitoSpyBean
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        UserCreateDto userCreateDto = UserCreateDto
            .builder()
            .firstname(USER_FIRSTNAME)
            .lastname(USER_LASTNAME)
            .email(USER_EMAIL)
            .build();

        UserCreateResponseDto responseDto = service.createUser(userCreateDto);

        assertNotNull(responseDto);
        assertNotNull(responseDto.id());
        verify(userRepository).save(captor.capture());
        var savedUser = captor.getValue();
        assertEquals(USER_FIRSTNAME, savedUser.getFirstname());
        assertEquals(USER_LASTNAME, savedUser.getLastname());
        assertEquals(USER_EMAIL, savedUser.getEmail());
        assertEquals(0, savedUser.getPostCount());
        assertTrue(savedUser.getActive());
    }

    @Test
    void createUser_alreadyExistEmail() throws IOException {
        loadSeedData();
        UserCreateDto userCreateDto = UserCreateDto
            .builder()
            .firstname(USER_FIRSTNAME)
            .lastname(USER_LASTNAME)
            .email(USER_EMAIL)
            .build();

        assertThrows(DuplicateKeyException.class, () -> service.createUser(userCreateDto));
    }

    @Test
    void getUser() throws IOException {
        loadSeedData();

        UserResponseDto userResponseDto = service.getUser(USER_ID);

        assertEquals(USER_ID, userResponseDto.id());
        assertEquals(USER_FIRSTNAME, userResponseDto.firstname());
        assertEquals(USER_LASTNAME, userResponseDto.lastname());
        assertEquals(USER_EMAIL, userResponseDto.email());
        assertEquals(3, userResponseDto.postCount());
        assertTrue(userResponseDto.active());
    }

    @Test
    void deleteUser() throws IOException {
        loadSeedData();
        UserResponseDto userResponseDto = service.getUser(USER_ID);
        assertEquals(USER_ID, userResponseDto.id());

        service.deleteUser(USER_ID);

        assertThrows(NotFoundException.class, () -> service.getUser(USER_ID));

        assertDoesNotThrow(() -> service.deleteUser(USER_ID));
    }

    @Test
    void modifyUser() throws IOException {
        loadSeedData();
        UserResponseDto userResponseDto = service.getUser(USER_ID);
        assertEquals(USER_ID, userResponseDto.id());
        UserModifyDto userModifyDto = UserModifyDto
            .builder()
            .firstname("Test")
            .lastname("Fake")
            .email("test@fake.com")
            .build();

        service.modifyUser(USER_ID, userModifyDto);

        userResponseDto = service.getUser(USER_ID);
        assertEquals(USER_ID, userResponseDto.id());
        assertEquals("Test", userResponseDto.firstname());
        assertEquals("Fake", userResponseDto.lastname());
        assertEquals("test@fake.com", userResponseDto.email());
    }
}
