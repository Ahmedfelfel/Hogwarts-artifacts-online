package com.felfel.hogwarts_artifacts_online.wizard;

import com.felfel.hogwarts_artifacts_online.system.Result;
import com.felfel.hogwarts_artifacts_online.wizard.coverter.WizardDtoToWizardConverter;
import com.felfel.hogwarts_artifacts_online.wizard.coverter.WizardToWizardDtoConverter;
import com.felfel.hogwarts_artifacts_online.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * The type Wizard controller.
 */
@RestController
@RequestMapping("${api.endpoint.base-url}")
public class WizardController {

    private final WizardService wizardService;
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;
    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

    /**
     * Instantiates a new Wizard controller.
     *
     * @param wizardService              the wizard service
     * @param wizardToWizardDtoConverter the wizard to wizard dto converter
     * @param wizardDtoToWizardConverter the wizard dto to wizard converter
     */
    public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }

    /**
     * Find all wizards result.
     *
     * @return the result
     */
    @GetMapping("/wizards")
    public Result findAllWizards()
    {
        List<Wizard> wizardList = wizardService.findAll();
        List<WizardDto> wizardDtoList = wizardList
                .stream()
                .map(wizardToWizardDtoConverter::convert)
                .toList();
        return new Result(true, HttpStatus.OK.value(),"find all success",wizardDtoList );
    }

    /**
     * Find wizard by id result.
     *
     * @param wizardId the wizard id
     * @return the result
     */
    @GetMapping("/wizards/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId)
    {
        Wizard foundWizard = wizardService.findById(wizardId);
        WizardDto wizardDto = wizardToWizardDtoConverter.convert(foundWizard);
        return new Result(true, HttpStatus.OK.value(), "find one success", wizardDto);
    }

    /**
     * Add wizard result.
     *
     * @param wizarddto the wizarddto
     * @return the result
     */
    @PostMapping("/wizards")
    public Result addWizard(@Valid @RequestBody WizardDto wizarddto)
    {
        Wizard wizard = wizardDtoToWizardConverter.convert(wizarddto);
        Wizard savedWizard = wizardService.saveWizard(wizard);
        WizardDto savedWizardDto = wizardToWizardDtoConverter.convert(savedWizard);
        return new Result(true, HttpStatus.CREATED.value(), "add success", savedWizardDto);
    }

    /**
     * Update wizard result.
     *
     * @param wizardDto the wizard dto
     * @param wizardId  the wizard id
     * @return the result
     */
    @PutMapping("/wizards/{wizardId}")
    public Result updateWizard(@Valid @RequestBody WizardDto wizardDto,@PathVariable Integer wizardId)
    {
        Wizard recivedWizard = wizardDtoToWizardConverter.convert(wizardDto);
        Wizard updatedWizard = wizardService.updateWizard(wizardId, recivedWizard);
        WizardDto updatedWizardDto = wizardToWizardDtoConverter.convert(updatedWizard);
        return new Result(true,HttpStatus.OK.value(), "update success",updatedWizardDto);
    }

    /**
     * Delete wizard result.
     *
     * @param wizardId the wizard id
     * @return the result
     */
    @DeleteMapping("/wizards/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId)
    {
        this.wizardService.deleteWizard(wizardId);
        return new Result(true,HttpStatus.OK.value(),"delete success");
    }

    /**
     * Assign artifact result.
     *
     * @param wizardId   the wizard id
     * @param artifactId the artifact id
     * @return the result
     */
    @PutMapping("/wizards/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact (@PathVariable Integer wizardId,@PathVariable String artifactId)
    {
        this.wizardService.assignArtifact(wizardId,artifactId);
        return new Result(true,HttpStatus.OK.value(),"assign artifact success");
    }
}
