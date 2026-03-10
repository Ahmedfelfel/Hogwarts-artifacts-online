package com.felfel.hogwarts_artifacts_online.artifact.coverter;

import com.felfel.hogwarts_artifacts_online.artifact.Artifact;
import com.felfel.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The type Artifact dto to artifact converter.
 */
@Component
public class ArtifactDtoToArtifactConverter implements Converter<ArtifactDto, Artifact> {

    @Override
    public Artifact convert(ArtifactDto source) {
        Artifact newArtifact = new Artifact();
        newArtifact.setId(source.id());
        newArtifact.setName(source.name());
        newArtifact.setDescription(source.description());
        newArtifact.setImageUrl(source.imageUrl());
        return newArtifact;
    }
}
