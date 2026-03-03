package com.felfel.hogwarts_artifacts_online.artifact;

public class ArtifactNotFoundException extends RuntimeException {
    public ArtifactNotFoundException(String artifactId) {
        super("Could not find artifact with ID " + artifactId + " :(");
    }
    public ArtifactNotFoundException()
    {
        super(("Could not find artifacts :("));
    }
}
