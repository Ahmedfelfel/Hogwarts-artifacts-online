package com.felfel.hogwarts_artifacts_online.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record AddUserDto(
                         @NotEmpty(message = "username is required")
                         String username,
                         @NotEmpty(message = "password is required")
                         String password,
                         Boolean enabled,
                         @NotEmpty(message = "roles are required")
                         String roles) {
}
