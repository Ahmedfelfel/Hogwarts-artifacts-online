package com.felfel.hogwarts_artifacts_online.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record UpdateUserDto (
        @NotEmpty(message = "username is required")
        String username,
        Boolean enabled,
        @NotEmpty(message = "roles are required")
        String roles){
}
