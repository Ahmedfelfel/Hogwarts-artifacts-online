package com.felfel.hogwarts_artifacts_online.artifact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Artifact repository.
 */
@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, String> {
}
