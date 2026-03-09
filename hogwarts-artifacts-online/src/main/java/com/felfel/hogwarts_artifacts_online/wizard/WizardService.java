package com.felfel.hogwarts_artifacts_online.wizard;

import com.felfel.hogwarts_artifacts_online.artifact.Artifact;
import com.felfel.hogwarts_artifacts_online.artifact.ArtifactRepository;
import com.felfel.hogwarts_artifacts_online.system.exception.OpjectNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WizardService {
    private final WizardRepository wizardRepository;
    private final ArtifactRepository artifactRepository;
    private final String OBJECT_TYPE="wizard";

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public List<Wizard> findAll() {
        List<Wizard> wizardList = this.wizardRepository.findAll();
        if(wizardList.isEmpty())
        {
            throw new OpjectNotFoundException(OBJECT_TYPE);
        }
        return wizardList;
    }

    public Wizard saveWizard(Wizard wizard) {
        return this.wizardRepository.save(wizard);
    }

    public Wizard findById(Integer wizardId) {
        return this.wizardRepository
                .findById(wizardId)
                .orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,wizardId));
    }

    public Wizard updateWizard(Integer wizardId, Wizard newWizard) {
        return wizardRepository.findById(wizardId)
                .map(wizard -> {
                    wizard.setName(newWizard.getName());
                    return this.wizardRepository.save(wizard);
                }).orElseThrow(() -> new OpjectNotFoundException(OBJECT_TYPE,wizardId)
                );
    }

    public void deleteWizard(Integer wizardId) {
        Wizard wizard=this.wizardRepository.findById(wizardId)
                .orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,wizardId));
        wizard.removeAllArtifact(wizard.getArtifacts());
        this.wizardRepository.deleteById(wizardId);
    }

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
