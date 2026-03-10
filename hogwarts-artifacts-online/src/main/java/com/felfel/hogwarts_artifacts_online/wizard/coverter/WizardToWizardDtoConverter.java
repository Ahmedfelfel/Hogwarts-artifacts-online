package com.felfel.hogwarts_artifacts_online.wizard.coverter;

import com.felfel.hogwarts_artifacts_online.wizard.Wizard;
import com.felfel.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The type Wizard to wizard dto converter.
 */
@Component
public class WizardToWizardDtoConverter implements Converter<Wizard, WizardDto> {
    @Override
    public WizardDto convert(Wizard source) {
        return new WizardDto(source.getId(),
                source.getName(),
                source.getNumberOfArtifacts());
    }
}
