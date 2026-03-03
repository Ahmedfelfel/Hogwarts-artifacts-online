package com.felfel.hogwarts_artifacts_online.wizard;

import com.felfel.hogwarts_artifacts_online.artifact.Artifact;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Wizard implements Serializable {

    @Id
    private Integer id;

    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},mappedBy = "owner")
    private List<Artifact> artifacts = new ArrayList<>();


    public void addArtifact(Artifact a) {
        a.setOwner(this);
        artifacts.add(a);
    }

    public Integer getNumberOfArtifacts() {
        return artifacts.size();
    }
}
