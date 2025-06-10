package it.swam.backend.service.impl;

import it.swam.backend.dto.UserCreateDto;
import it.swam.backend.dto.UserCreateResponseDto;
import it.swam.backend.dto.UserDtoMapper;
import it.swam.backend.dto.UserModifyDto;
import it.swam.backend.dto.UserResponseDto;
import it.swam.backend.entity.User;
import it.swam.backend.exception.NotFoundException;
import it.swam.backend.repository.UserRepository;
import it.swam.backend.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND = "User with id %s not found";
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Override
    public UserCreateResponseDto createUser(UserCreateDto userCreateDto) {
        User user = userDtoMapper.toEntity(userCreateDto).setActive(true);
        return userDtoMapper.toCreateResponseDto(userRepository.save(user));
    }

    @SneakyThrows
    @Override
    public UserResponseDto getUser(String id) {
        var user = findUser(id);
        return userDtoMapper.toDto(user);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        return userDtoMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void modifyUser(String id, UserModifyDto modifyDto) {
        var user = findUser(id);
        user.setFirstname(modifyDto.firstname())
            .setLastname(modifyDto.lastname())
            .setEmail(modifyDto.email());
        userRepository.save(user);
    }

    private User findUser(String id) {
        return userRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND, id)));
    }
}
