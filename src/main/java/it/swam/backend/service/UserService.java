package it.swam.backend.service;

import it.swam.backend.dto.UserCreateDto;
import it.swam.backend.dto.UserCreateResponseDto;
import it.swam.backend.dto.UserModifyDto;
import it.swam.backend.dto.UserResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.validation.annotation.Validated;


@Validated
public interface UserService {

    UserCreateResponseDto createUser(@Valid UserCreateDto authorCreateDto);

    UserResponseDto getUser(@NotBlank String id);

    List<UserResponseDto> getUsers();

    void deleteUser(@NotBlank String id);

    void modifyUser(@NotBlank String id, UserModifyDto modifyDto);
}
