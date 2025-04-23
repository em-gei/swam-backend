package it.swam.backend.dto;

import it.swam.backend.entity.User;
import org.mapstruct.Mapper;


@Mapper
public interface UserDtoMapper {

    User toEntity(UserCreateDto dto);

    UserResponseDto toDto(User user);

    UserCreateResponseDto toCreateResponseDto(User user);
}
