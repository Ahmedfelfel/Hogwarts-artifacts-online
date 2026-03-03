package com.felfel.hogwarts_artifacts_online.system;

import com.felfel.hogwarts_artifacts_online.artifact.Artifact;
import com.felfel.hogwarts_artifacts_online.artifact.ArtifactRepository;
import com.felfel.hogwarts_artifacts_online.wizard.Wizard;
import com.felfel.hogwarts_artifacts_online.wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;

    private final WizardRepository wizardRepository;

    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Wizard w1 = new Wizard();
        Wizard w2 = new Wizard();
        w1.setName("ahmed");
        w1.setId(1);
        w2.setName("felfel");
        w2.setId(2);

        Artifact a1 =new Artifact();
        a1.setId("1234567890ABCDEF");
        a1.setName("Deluminator");
        a1.setDescription("A device used to absorb light from a place and then release it.");
        a1.setImageUrl("imageUrl");

        Artifact a2 =new Artifact();
        a2.setId("1234567890ABCDE1");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An enchanted cloak that makes the wearer invisible.");
        a2.setImageUrl("imageUrl");

        Artifact a3 =new Artifact();
        a3.setId("1234567890ABCDE2");
        a3.setName("Elder Wand");
        a3.setDescription("The most powerful wand in existence.");
        a3.setImageUrl("imageUrl");

        Artifact a4 =new Artifact();
        a4.setId("1234567890ABCDE3");
        a4.setName("Marauder's Map");
        a4.setDescription("A magical map of Hogwarts that shows the location of everyone in the castle.");
        a4.setImageUrl("imageUrl");

        w1.addArtifact(a1);
        w1.addArtifact(a2);
        w2.addArtifact(a3);

        wizardRepository.save(w1);
        wizardRepository.save(w2);
        artifactRepository.save(a4);
    }
}
