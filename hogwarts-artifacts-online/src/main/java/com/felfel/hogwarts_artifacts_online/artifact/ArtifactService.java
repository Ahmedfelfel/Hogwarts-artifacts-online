package com.felfel.hogwarts_artifacts_online.artifact;


import com.felfel.hogwarts_artifacts_online.artifact.utils.IdWorker;
import com.felfel.hogwarts_artifacts_online.system.exception.OpjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class ArtifactService {

    private final String OBJECT_TYPE="artifact";
    private final ArtifactRepository artifactRepository;
    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactRepository,IdWorker idWorker) {
        this.artifactRepository = artifactRepository;
        this.idWorker=idWorker;
    }

    public Artifact findById(String artifactId)
    {
        return this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new OpjectNotFoundException(OBJECT_TYPE,artifactId));
    }

    public List<Artifact> findAll()
    {
        List<Artifact> artifacts = this.artifactRepository.findAll();
        if(artifacts.isEmpty())
        {
            throw new OpjectNotFoundException(OBJECT_TYPE);
        }
         return artifacts;
    }

    public Artifact saveArtifact(Artifact newArtifact)
    {
        newArtifact.setId((idWorker.nextId())+"");
        return this.artifactRepository.save(newArtifact);
    }
    public Artifact updateArtifact(String ArtifactId,Artifact updatedArtifact)
    {
        return this.artifactRepository.findById(ArtifactId)
            .map(artifact -> {
            artifact.setName(updatedArtifact.getName());
            artifact.setDescription(updatedArtifact.getDescription());
            artifact.setImageUrl(updatedArtifact.getImageUrl());
            return this.artifactRepository.save(artifact);
        }).orElseThrow(() -> new OpjectNotFoundException(OBJECT_TYPE,ArtifactId));
    }

    public void deleteArtifact(String artifactId) {
        Artifact artifact = this.artifactRepository.findById(artifactId)
                .orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,artifactId));
        this.artifactRepository.deleteById(artifactId);
    }
}
