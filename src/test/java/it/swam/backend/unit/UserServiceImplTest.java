package it.swam.backend.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.swam.backend.common.BaseTest;
import it.swam.backend.dto.UserCreateDto;
import it.swam.backend.dto.UserCreateResponseDto;
import it.swam.backend.dto.UserDtoMapper;
import it.swam.backend.dto.UserModifyDto;
import it.swam.backend.dto.UserResponseDto;
import it.swam.backend.entity.User;
import it.swam.backend.repository.UserRepository;
import it.swam.backend.service.impl.UserServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest extends BaseTest {

    @InjectMocks
    UserServiceImpl service;
    @Mock
    UserRepository userRepository;
    @Mock
    UserDtoMapper userDtoMapper;

    @Test
    void createUserTest() {
        UserCreateDto createUserDto = UserCreateDto
            .builder()
            .firstname(USER_FIRSTNAME)
            .lastname(USER_LASTNAME)
            .email(USER_EMAIL)
            .build();
        User user = givenUser(null);
        when(userDtoMapper.toEntity(createUserDto)).thenReturn(user);
        when(userRepository.save(user)).then(argument -> {
            var savedUser = argument.getArgument(0, User.class);
            savedUser.setId(USER_ID);
            return savedUser;
        });
        when(userDtoMapper.toCreateResponseDto(user)).then(argument -> {
            var savedUser = argument.getArgument(0, User.class);
            return UserCreateResponseDto.builder().id(savedUser.getId()).build();
        });

        UserCreateResponseDto outcome = service.createUser(createUserDto);

        verify(userRepository).save(any());
        assertNotNull(outcome);
        assertEquals(USER_ID, outcome.id());
    }

    @Test
    void getUserTest() {
        var user = givenUser(USER_ID);
        user.setPostCount(5);
        user.setActive(true);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userDtoMapper.toDto(user)).thenReturn(UserResponseDto
            .builder()
            .id(user.getId())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .email(user.getEmail())
            .active(user.getActive())
            .postCount(user.getPostCount())
            .build());

        UserResponseDto userResponseDto = service.getUser(USER_ID);

        assertEquals(USER_ID, userResponseDto.id());
        assertEquals(USER_FIRSTNAME, userResponseDto.firstname());
        assertEquals(USER_LASTNAME, userResponseDto.lastname());
        assertEquals(5, userResponseDto.postCount());
        assertTrue(userResponseDto.active());
    }

    @Test
    void getUserListTest() {
        var user = givenUser(USER_ID);
        user.setPostCount(5);
        user.setActive(true);
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userDtoMapper.toDtoList(any())).thenReturn(List.of(UserResponseDto
            .builder()
            .id(user.getId())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .email(user.getEmail())
            .active(user.getActive())
            .postCount(user.getPostCount())
            .build()));

        List<UserResponseDto> userResponseDtoList = service.getUsers();

        assertNotNull(userResponseDtoList);
        assertEquals(1, userResponseDtoList.size());
        assertEquals(USER_ID, userResponseDtoList.getFirst().id());
        assertEquals(USER_FIRSTNAME, userResponseDtoList.getFirst().firstname());
        assertEquals(USER_LASTNAME, userResponseDtoList.getFirst().lastname());
        assertEquals(5, userResponseDtoList.getFirst().postCount());
        assertTrue(userResponseDtoList.getFirst().active());
    }

    @Test
    void deleteUserTest() {
        service.deleteUser(USER_ID);

        verify(userRepository).deleteById(USER_ID);
    }

    @Test
    void modifyUserTest() {
        var user = givenUser(USER_ID);
        user.setPostCount(5);
        user.setActive(true);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        UserModifyDto userModifyDto = UserModifyDto.builder()
            .firstname("anotherFirstname")
            .lastname("anotherLastname")
            .email("anotherEmail@test.com")
            .build();

        service.modifyUser(USER_ID, userModifyDto);

        verify(userRepository).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        assertEquals(userModifyDto.firstname(), updatedUser.getFirstname());
        assertEquals(userModifyDto.lastname(), updatedUser.getLastname());
        assertEquals(userModifyDto.email(), updatedUser.getEmail());
    }

    private User givenUser(String id) {
        return User
            .builder()
            .id(id)
            .firstname(USER_FIRSTNAME)
            .lastname(USER_LASTNAME)
            .email(USER_EMAIL)
            .rowDate(LocalDateTime.now())
            .build();
    }
}
