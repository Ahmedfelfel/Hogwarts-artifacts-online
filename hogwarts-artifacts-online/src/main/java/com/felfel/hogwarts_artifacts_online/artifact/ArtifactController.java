package com.felfel.hogwarts_artifacts_online.artifact;

import com.felfel.hogwarts_artifacts_online.artifact.coverter.ArtifactDtoToArtifactConverter;
import com.felfel.hogwarts_artifacts_online.artifact.coverter.ArtifactToArtifactDtoConverter;
import com.felfel.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import com.felfel.hogwarts_artifacts_online.system.Result;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * The type Artifact controller.
 */
@RestController
@RequestMapping("${api.endpoint.base-url}")
public class ArtifactController {

private final ArtifactService artifactService;
private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    /**
     * Instantiates a new Artifact controller.
     *
     * @param artifactService                the artifact service
     * @param artifactToArtifactDtoConverter the artifact to artifact dto converter
     * @param artifactDtoToArtifactConverter the artifact dto to artifact converter
     */
    public ArtifactController(ArtifactService artifactService,
                              ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter,
                              ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter)
    {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }

    /**
     * Find all artifacts result.
     *
     * @return the result
     */
    @GetMapping("/artifacts")
    @ResponseStatus(HttpStatus.OK)
    public Result findAllArtifacts() {
        List<Artifact> artifacts = artifactService.findAll();
        List<ArtifactDto> foundArtifactDtos = artifacts
                .stream()
                .map(artifactToArtifactDtoConverter::convert)
                .toList();
        return new Result(true, HttpStatus.OK.value(), "find all success", foundArtifactDtos);
    }

    /**
     * Find artifact by id result.
     *
     * @param artifactId the artifact id
     * @return the result
     */
    @GetMapping("/artifacts/{artifactId}")
    @ResponseStatus(HttpStatus.OK)
    public Result findArtifactById(@PathVariable String artifactId) {
        Artifact foundArtifact = artifactService.findById(artifactId);
        ArtifactDto artifactDto = artifactToArtifactDtoConverter.convert(foundArtifact);
        return new Result(true, HttpStatus.OK.value(), "find one success",artifactDto );
    }

    /**
     * Add artifact result.
     *
     * @param artifactDto the artifact dto
     * @return the result
     */
    @SuppressWarnings("DataFlowIssue")
    @PostMapping("/artifacts")
    @ResponseStatus(HttpStatus.CREATED)
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto)
    {
        Artifact recivedArtifact = artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact savedArtifact = artifactService.saveArtifact(recivedArtifact);
        ArtifactDto savedArtifactDto = artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true,
                        HttpStatus.CREATED.value(),
                        "add success",
                        savedArtifactDto);
    }

    /**
     * Update artifact result.
     *
     * @param artifactId  the artifact id
     * @param artifactDto the artifact dto
     * @return the result
     */
    @PutMapping("/artifacts/{artifactId}")
    @ResponseStatus(HttpStatus.OK)
    public Result updateArtifact(@PathVariable String artifactId,@Valid @RequestBody ArtifactDto artifactDto)
    {
        Artifact recivedArtifact = artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact updatedArtifact = artifactService.updateArtifact(artifactId,recivedArtifact);
        ArtifactDto updatedArtifactDto = artifactToArtifactDtoConverter.convert(updatedArtifact);
        return new Result(true,
                HttpStatus.OK.value(),
                "update success",
                updatedArtifactDto);
    }

    /**
     * Delete artifact result.
     *
     * @param artifactId the artifact id
     * @return the result
     */
    @DeleteMapping("/artifacts/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId)
    {
     this.artifactService.deleteArtifact(artifactId);
     return new Result(true,HttpStatus.OK.value(), "delete success");
    }
}