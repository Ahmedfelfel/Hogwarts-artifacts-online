package com.felfel.hogwarts_artifacts_online.wizard;

public class WizardNotFoundException extends RuntimeException{
    public WizardNotFoundException(Integer wizardId) {
        super("Could not find wizard with ID " + wizardId + " :(");
    }
    public WizardNotFoundException()
    {
        super(("Could not find wizards :("));
    }
}
