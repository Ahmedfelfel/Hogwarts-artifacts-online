package com.felfel.hogwarts_artifacts_online.wizard;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WizardService {
    private final WizardRepository wizardRepository;

    public WizardService(WizardRepository wizardRepository) {
        this.wizardRepository = wizardRepository;
    }

    public List<Wizard> findAll() {
        List<Wizard> wizardList = this.wizardRepository.findAll();
        if(wizardList.isEmpty())
        {
            throw new WizardNotFoundException();
        }
        return wizardList;
    }

    public Wizard saveWizard(String wizardName) {
        Wizard newWizard = new Wizard();
        newWizard.setName(wizardName);
        return this.wizardRepository.save(newWizard);
    }

    public Wizard findById(Integer wizardId) {
        return this.wizardRepository
                .findById(wizardId)
                .orElseThrow(()->new WizardNotFoundException(wizardId));
    }

    public Wizard updateWizard(Integer wizardId, @NotEmpty String newWizardName) {
        return wizardRepository.findById(wizardId)
                .map(wizard -> {
                    wizard.setName(newWizardName);
                    return this.wizardRepository.save(wizard);
                }).orElseThrow(() -> new WizardNotFoundException(wizardId)
                );
    }

    public void deleteWizard(Integer wizardId) {
        this.wizardRepository.findById(wizardId)
                .orElseThrow(()->new WizardNotFoundException(wizardId));
        this.wizardRepository.deleteById(wizardId);
    }
}
