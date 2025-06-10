package it.swam.backend.integration.ws;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import java.util.Arrays;
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
import org.springframework.security.test.context.support.WithMockUser;
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

    @Test
    void createUser_missingAuth() throws Exception {
        mockMvc
            .perform(post("/users").with(csrf()).content(getBody(givenUserCreateDto())).contentType(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", password = "pass")
    void creatUser_missingBody() {
        mockMvc.perform(post("/users").with(csrf()).contentType(APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("invalidUserCreateDto")
    @WithMockUser(username = "user", password = "pass")
    void createUser_invalidDto(UserCreateDto userCreateDto) {
        mockMvc
            .perform(post("/users").with(csrf()).content(getBody(userCreateDto)).contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidUserCreateDto() {
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
    @WithMockUser(username = "user", password = "pass")
    void createUser() {
        var userCreateDto = givenUserCreateDto();
        mockMvc
            .perform(post("/users").with(csrf()).content(getBody(userCreateDto)).contentType(APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(service).createUser(userCreateDto);
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", password = "pass")
    void getUser_notFoundException() {
        when(service.getUser(USER_ID)).thenThrow(new NotFoundException("user not found"));

        mockMvc
            .perform(get("/users/{id}", USER_ID).with(csrf()).contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getUser_missingAuth() {
        mockMvc
            .perform(get("/users/{id}", USER_ID)
                .with(csrf())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", password = "pass")
    void getUser() {
        UserResponseDto userResponseDto = UserResponseDto
            .builder()
            .id(USER_ID)
            .firstname(USER_FIRSTNAME)
            .lastname(USER_LASTNAME)
            .email(USER_EMAIL)
            .active(true)
            .postCount(1)
            .build();
        when(service.getUser(USER_ID)).thenReturn(userResponseDto);

        mockMvc
            .perform(get("/users/{id}", USER_ID).with(csrf()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstname").value(USER_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(USER_LASTNAME))
            .andExpect(jsonPath("$.email").value(USER_EMAIL))
            .andExpect(jsonPath("$.active").value(true))
            .andExpect(jsonPath("$.postCount").value(1));
    }

    @SneakyThrows
    @Test
    void getUserList_missingAuth() {
        mockMvc
            .perform(get("/users", USER_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", password = "pass")
    void getUserList() {
        UserResponseDto userResponseDto = UserResponseDto
            .builder()
            .id(USER_ID)
            .firstname(USER_FIRSTNAME)
            .lastname(USER_LASTNAME)
            .email(USER_EMAIL)
            .active(true)
            .postCount(1)
            .build();
        when(service.getUsers()).thenReturn(Arrays.asList(userResponseDto));

        mockMvc
            .perform(get("/users", USER_ID).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].firstname").value(USER_FIRSTNAME))
            .andExpect(jsonPath("$[0].lastname").value(USER_LASTNAME))
            .andExpect(jsonPath("$[0].email").value(USER_EMAIL))
            .andExpect(jsonPath("$[0].active").value(true))
            .andExpect(jsonPath("$[0].postCount").value(1));
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", password = "pass")
    void deleteUser() {
        mockMvc
            .perform(delete("/users/{id}", USER_ID).with(csrf()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void deleteUser_missingAuth() {
        mockMvc
            .perform(delete("/users/{id}", USER_ID).with(csrf()).contentType(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", password = "pass")
    void modifyUser_missingBody() {
        mockMvc
            .perform(put("/users/{id}", USER_ID).with(csrf()).contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("invalidUserModifyDto")
    @WithMockUser(username = "user", password = "pass")
    void modifyUser_invalidDto(UserModifyDto userModifyDto) {
        mockMvc
            .perform(put("/users/{id}", USER_ID)
                .with(csrf())
                .content(getBody(userModifyDto))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidUserModifyDto() {
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
    @WithMockUser(username = "user", password = "pass")
    void modifyUser() {
        var userModifyDto = givenUserModifyDto();
        mockMvc
            .perform(put("/users/{id}", USER_ID)
                .with(csrf())
                .content(getBody(userModifyDto))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(service).modifyUser(USER_ID, userModifyDto);
    }

    @SneakyThrows
    @Test
    void modifyUser_missingAuth() {
        mockMvc
            .perform(put("/users/{id}", USER_ID)
                .with(csrf())
                .content(getBody(givenUserModifyDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
}
