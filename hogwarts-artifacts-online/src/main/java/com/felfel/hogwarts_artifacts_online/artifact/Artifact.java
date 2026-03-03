package com.felfel.hogwarts_artifacts_online.artifact;

import com.felfel.hogwarts_artifacts_online.wizard.Wizard;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artifact implements Serializable {
    @Id
    private String id;

    private String name;

    private String description;
    
    private String imageUrl;

    @ManyToOne
    private Wizard owner;
}
