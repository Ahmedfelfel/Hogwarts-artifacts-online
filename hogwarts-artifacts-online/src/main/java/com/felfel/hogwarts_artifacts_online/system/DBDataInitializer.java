package com.felfel.hogwarts_artifacts_online.system;

import com.felfel.hogwarts_artifacts_online.artifact.Artifact;
import com.felfel.hogwarts_artifacts_online.artifact.ArtifactRepository;
import com.felfel.hogwarts_artifacts_online.user.User;
import com.felfel.hogwarts_artifacts_online.user.UserService;
import com.felfel.hogwarts_artifacts_online.wizard.Wizard;
import com.felfel.hogwarts_artifacts_online.wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * The type Db data initializer.
 */
@SuppressWarnings("RedundantThrows")
@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;

    private final WizardRepository wizardRepository;

    private final UserService userService;

    /**
     * Instantiates a new Db data initializer.
     *
     * @param artifactRepository the artifact repository
     * @param wizardRepository   the wizard repository
     * @param userService        the user service
     */
    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository, UserService userService) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
        this.userService = userService;

    }

    @Override
    public void run(String... args) throws Exception {
        Wizard w1 = new Wizard();
        Wizard w2 = new Wizard();
        w1.setName("ahmed");
        w2.setName("felfel");

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

        User u1 = new User();
        u1.setUsername("john");
        u1.setEnabled(true);
        u1.setRoles("admin user");
        u1.setPassword("123");
        User u2 = new User();
        u2.setUsername("eric");
        u2.setEnabled(true);
        u2.setRoles("user");
        u2.setPassword("123");
        User u3 = new User();
        u3.setUsername("tom");
        u3.setEnabled(false);
        u3.setRoles("user");
        u3.setPassword("123");

        wizardRepository.save(w1);
        wizardRepository.save(w2);
        artifactRepository.save(a4);

        userService.saveUser(u1);
        userService.saveUser(u2);
        userService.saveUser(u3);
    }
}
