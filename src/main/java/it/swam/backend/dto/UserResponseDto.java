package it.swam.backend.dto;

import lombok.Builder;


@Builder
public record UserResponseDto(
    String id,
    String firstname,
    String lastname,
    String email,
    Integer postCount,
    Boolean active
) {}
