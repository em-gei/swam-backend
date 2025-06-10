package it.swam.backend.dto;

import it.swam.backend.entity.User;
import java.util.List;
import org.mapstruct.Mapper;


@Mapper
public interface UserDtoMapper {

    User toEntity(UserCreateDto dto);

    UserResponseDto toDto(User user);

    List<UserResponseDto> toDtoList(List<User> userList);

    UserCreateResponseDto toCreateResponseDto(User user);
}
