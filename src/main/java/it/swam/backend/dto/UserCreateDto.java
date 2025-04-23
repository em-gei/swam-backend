package it.swam.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


@Builder
public record UserCreateDto(
    @NotBlank String firstname,
    @NotBlank String lastname,
    @Email @NotBlank String email
) {}
