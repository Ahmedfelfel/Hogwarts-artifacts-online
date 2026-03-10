package com.felfel.hogwarts_artifacts_online.wizard.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * The type Wizard dto.
 */
public record WizardDto(Integer id,
                        @NotEmpty(message = "Name is required")
                        String name,
                        Integer numberOfArtifacts) { }
