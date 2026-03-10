package com.felfel.hogwarts_artifacts_online.wizard;

import com.felfel.hogwarts_artifacts_online.artifact.Artifact;
import com.felfel.hogwarts_artifacts_online.artifact.ArtifactRepository;
import com.felfel.hogwarts_artifacts_online.system.exception.OpjectNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The type Wizard service.
 */
@Service
public class WizardService {
    private final WizardRepository wizardRepository;
    private final ArtifactRepository artifactRepository;
    private final String OBJECT_TYPE="wizard";

    /**
     * Instantiates a new Wizard service.
     *
     * @param wizardRepository   the wizard repository
     * @param artifactRepository the artifact repository
     */
    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Wizard> findAll() {
        List<Wizard> wizardList = this.wizardRepository.findAll();
        if(wizardList.isEmpty())
        {
            throw new OpjectNotFoundException(OBJECT_TYPE);
        }
        return wizardList;
    }

    /**
     * Save wizard wizard.
     *
     * @param wizard the wizard
     * @return the wizard
     */
    public Wizard saveWizard(Wizard wizard) {
        return this.wizardRepository.save(wizard);
    }

    /**
     * Find by id wizard.
     *
     * @param wizardId the wizard id
     * @return the wizard
     */
    public Wizard findById(Integer wizardId) {
        return this.wizardRepository
                .findById(wizardId)
                .orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,wizardId));
    }

    /**
     * Update wizard wizard.
     *
     * @param wizardId  the wizard id
     * @param newWizard the new wizard
     * @return the wizard
     */
    public Wizard updateWizard(Integer wizardId, Wizard newWizard) {
        return wizardRepository.findById(wizardId)
                .map(wizard -> {
                    wizard.setName(newWizard.getName());
                    return this.wizardRepository.save(wizard);
                }).orElseThrow(() -> new OpjectNotFoundException(OBJECT_TYPE,wizardId)
                );
    }

    /**
     * Delete wizard.
     *
     * @param wizardId the wizard id
     */
    public void deleteWizard(Integer wizardId) {
        Wizard wizard=this.wizardRepository.findById(wizardId)
                .orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,wizardId));
        wizard.removeAllArtifact(wizard.getArtifacts());
        this.wizardRepository.deleteById(wizardId);
    }

    /**
     * Assign artifact.
     *
     * @param wizardId   the wizard id
     * @param artifactId the artifact id
     */
    public void assignArtifact(Integer wizardId, String artifactId) {
        Artifact artifactToBeAssigned = artifactRepository.findById(artifactId)
                .orElseThrow(()->new OpjectNotFoundException("artifact",artifactId));
        Wizard wizard = wizardRepository.findById(wizardId)
                .orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,wizardId));
        if(artifactToBeAssigned.getOwner()!=null)
        {
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
        }
        wizard.addArtifact(artifactToBeAssigned);
    }
}
