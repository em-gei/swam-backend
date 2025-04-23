package it.swam.backend.integration.ws;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.swam.backend.common.BaseTest;
import it.swam.backend.dto.UserCreateDto;
import it.swam.backend.dto.UserModifyDto;
import it.swam.backend.dto.UserResponseDto;
import it.swam.backend.exception.NotFoundException;
import it.swam.backend.service.UserService;
import it.swam.backend.ws.UserController;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserControllerITest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService service;

    @SneakyThrows
    @Test
    void creatUser_missingBody() {
        mockMvc.perform(post("/users").contentType(APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("givenUserCreateDto")
    void createUser_invalidDto(UserCreateDto userCreateDto) {
        mockMvc
            .perform(post("/users").content(getBody(userCreateDto)).contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> givenUserCreateDto() {
        return Stream.of(
            Arguments.of(UserCreateDto.builder().firstname(USER_FIRSTNAME).lastname(USER_LASTNAME).build()),
            Arguments.of(UserCreateDto
                .builder()
                .firstname(USER_FIRSTNAME)
                .lastname(USER_LASTNAME)
                .email("wrong_format.com")
                .build()),
            Arguments.of(UserCreateDto.builder().firstname(USER_FIRSTNAME).email(USER_EMAIL).build()),
            Arguments.of(UserCreateDto.builder().lastname(USER_LASTNAME).email(USER_EMAIL).build())
        );
    }

    @SneakyThrows
    @Test
    void createUser() {
        var userCreateDto = UserCreateDto
            .builder()
            .firstname(USER_FIRSTNAME)
            .lastname(USER_LASTNAME)
            .email(USER_EMAIL)
            .build();
        mockMvc
            .perform(post("/users").content(getBody(userCreateDto)).contentType(APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(service).createUser(userCreateDto);
    }

    @SneakyThrows
    @Test
    void getUser_notFoundException() {
        when(service.getUser(USER_ID)).thenThrow(new NotFoundException("user not found"));

        mockMvc
            .perform(get("/users/{id}", USER_ID).contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getUser() {
        UserResponseDto userResponseDto = UserResponseDto.builder()
            .id(USER_ID)
            .firstname(USER_FIRSTNAME)
            .lastname(USER_LASTNAME)
            .email(USER_EMAIL)
            .active(true)
            .postCount(1)
            .build();
        when(service.getUser(USER_ID)).thenReturn(userResponseDto);

        mockMvc
            .perform(get("/users/{id}", USER_ID).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstname").value(USER_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(USER_LASTNAME))
            .andExpect(jsonPath("$.email").value(USER_EMAIL))
            .andExpect(jsonPath("$.active").value(true))
            .andExpect(jsonPath("$.postCount").value(1));
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        mockMvc
            .perform(delete("/users/{id}", USER_ID).contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void modifyUser_missingBody() {
        mockMvc
            .perform(put("/users/{id}", USER_ID)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("givenUserModifyDto")
    void modifyUser_invalidDto(UserModifyDto userModifyDto) {
        mockMvc
            .perform(put("/users/{id}", USER_ID).content(getBody(userModifyDto)).contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> givenUserModifyDto() {
        return Stream.of(
            Arguments.of(UserModifyDto.builder().firstname(USER_FIRSTNAME).lastname(USER_LASTNAME).build()),
            Arguments.of(UserModifyDto
                .builder()
                .firstname(USER_FIRSTNAME)
                .lastname(USER_LASTNAME)
                .email("wrong_format.com")
                .build()),
            Arguments.of(UserModifyDto.builder().firstname(USER_FIRSTNAME).email(USER_EMAIL).build()),
            Arguments.of(UserModifyDto.builder().lastname(USER_LASTNAME).email(USER_EMAIL).build())
        );
    }

    @SneakyThrows
    @Test
    void modifyUser() {
        var userModifyDto = UserModifyDto
            .builder()
            .firstname(USER_FIRSTNAME)
            .lastname(USER_LASTNAME)
            .email(USER_EMAIL)
            .build();
        mockMvc
            .perform(put("/users/{id}", USER_ID).content(getBody(userModifyDto)).contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(service).modifyUser(USER_ID, userModifyDto);
    }
}
