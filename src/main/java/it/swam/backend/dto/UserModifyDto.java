package it.swam.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


@Builder
public record UserModifyDto(
    @NotBlank String firstname,
    @NotBlank String lastname,
    @NotBlank @Email String email
) {}
