package com.felfel.hogwarts_artifacts_online.wizard;

import com.felfel.hogwarts_artifacts_online.artifact.Artifact;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Wizard.
 */
@Entity
@Data
@NoArgsConstructor
public class Wizard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},mappedBy = "owner")
    private List<Artifact> artifacts = new ArrayList<>();


    /**
     * Add artifact.
     *
     * @param a the a
     */
    public void addArtifact(Artifact a) {
        a.setOwner(this);
        artifacts.add(a);
    }

    /**
     * Gets number of artifacts.
     *
     * @return the number of artifacts
     */
    public Integer getNumberOfArtifacts() {
        return artifacts.size();
    }

    /**
     * Remove artifact.
     *
     * @param artifactToBeAssigned the artifact to be assigned
     */
    public void removeArtifact(Artifact artifactToBeAssigned) {
        artifactToBeAssigned.setOwner(null);
        this.artifacts.remove(artifactToBeAssigned);
    }

    /**
     * Remove all artifact.
     *
     * @param artifacts the artifacts
     */
    public void removeAllArtifact(List<Artifact> artifacts) {
        for (Artifact artifact : artifacts)
        {
            artifact.setOwner(null);
        }
        this.artifacts.clear();
    }
}
