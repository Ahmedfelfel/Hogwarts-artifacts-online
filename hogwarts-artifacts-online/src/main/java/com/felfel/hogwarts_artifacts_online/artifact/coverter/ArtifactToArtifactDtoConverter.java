package com.felfel.hogwarts_artifacts_online.artifact.coverter;

import com.felfel.hogwarts_artifacts_online.artifact.Artifact;
import com.felfel.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import com.felfel.hogwarts_artifacts_online.wizard.coverter.WizardToWizardDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The type Artifact to artifact dto converter.
 */
@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {
    /**
     * The Wizard to wizard dto converter.
     */
    WizardToWizardDtoConverter wizardToWizardDtoConverter;

    /**
     * Instantiates a new Artifact to artifact dto converter.
     *
     * @param wizardToWizardDtoConverter the wizard to wizard dto converter
     */
    public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        return new ArtifactDto(source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageUrl(),
                source.getOwner() != null
                        ? wizardToWizardDtoConverter.convert(source.getOwner()) : null);
    }
}
