package com.felfel.hogwarts_artifacts_online.wizard;

import com.felfel.hogwarts_artifacts_online.system.Result;
import com.felfel.hogwarts_artifacts_online.wizard.coverter.WizardToWizardDtoConverter;
import com.felfel.hogwarts_artifacts_online.wizard.dto.WizardDto;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class WizardController {

    private final WizardService wizardService;
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @GetMapping("wizards")
    public Result findAllWizards()
    {
        List<Wizard> wizardList = wizardService.findAll();
        List<WizardDto> wizardDtoList = wizardList
                .stream()
                .map(wizardToWizardDtoConverter::convert)
                .toList();
        return new Result(true, HttpStatus.OK.value(),"find all success",wizardDtoList );
    }
    @GetMapping("/wizards/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId)
    {
        Wizard foundWizard = wizardService.findById(wizardId);
        WizardDto wizardDto = wizardToWizardDtoConverter.convert(foundWizard);
        return new Result(true, HttpStatus.OK.value(), "find one success", wizardDto);
    }
    @PostMapping("/wizards")
    public Result addWizard(@NotEmpty @RequestParam String wizardName)
    {
        Wizard savedWizard = wizardService.saveWizard(wizardName);
        WizardDto savedWizardDto = wizardToWizardDtoConverter.convert(savedWizard);
        return new Result(true, HttpStatus.CREATED.value(), "add success", savedWizardDto);
    }
    @PutMapping("/wizards/{wizardId}")
    public Result updateWizard(@NotEmpty @RequestParam String newWizardName,@PathVariable Integer wizardId)
    {
        Wizard updatedWizard = wizardService.updateWizard(wizardId, newWizardName);
        WizardDto updatedWizardDto = wizardToWizardDtoConverter.convert(updatedWizard);
        return new Result(true,HttpStatus.OK.value(), "update success",updatedWizardDto);
    }
    @DeleteMapping("/wizards/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId)
    {
        this.wizardService.deleteWizard(wizardId);
        return new Result(true,HttpStatus.OK.value(),"delete success");
    }
}
