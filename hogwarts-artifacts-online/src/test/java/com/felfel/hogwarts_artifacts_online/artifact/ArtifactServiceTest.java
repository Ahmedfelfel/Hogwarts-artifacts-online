package com.felfel.hogwarts_artifacts_online.artifact;

import com.felfel.hogwarts_artifacts_online.artifact.utils.IdWorker;
import com.felfel.hogwarts_artifacts_online.system.exception.OpjectNotFoundException;
import com.felfel.hogwarts_artifacts_online.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    private ArtifactRepository artifactRepository;

    @Mock
    private IdWorker idWorker;

    @InjectMocks
    private ArtifactService artifactService;


    List<Artifact> artifactList = new ArrayList<>();

    @BeforeEach
    void setUp() {
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

        artifactList.addAll(List.of(a1,a2,a3,a4));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //given
        Artifact a = new Artifact();
        a.setId("16135131");
        a.setName("Invisibility Cloak");
        a.setDescription("any desc for testing");
        a.setImageUrl("ImageUrl");
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Ahmed");
        a.setOwner(w); //
        given(artifactRepository.findById("16135131")).willReturn(Optional.of(a));
        //when
        Artifact foundArtifact = artifactService.findById("16135131");
        //assert
        assertThat(foundArtifact.getId()).isEqualTo(a.getId()); // This line is correct
        assertThat(foundArtifact.getName()).isEqualTo(a.getName());
        assertThat(foundArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(foundArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
        verify(artifactRepository,times(1)).findById("16135131");
    }
    @Test
    void testFindByIdNotFound()
    {
        //given
        given(artifactRepository.findById(Mockito.anyString())).willReturn(Optional.empty());
        //when
        Throwable thrown = catchThrowable(() -> artifactService.findById("16135131"));
        //assert
        assertThat(thrown).isInstanceOf(OpjectNotFoundException.class).hasMessage("Could not find artifact with ID 16135131 :(");
    }
    @Test
    void testFindAllSuccess()
    {
        //given
        given(artifactRepository.findAll()).willReturn(this.artifactList);
        //when
        List<Artifact> returnedArtifacts = artifactService.findAll();
        //then
        assertThat(returnedArtifacts).isEqualTo(this.artifactList);
        verify(artifactRepository,times(1)).findAll();
    }

    @Test
    void testFindAllFailed()
    {
        //given
        given(artifactRepository.findAll()).willReturn(new ArrayList<>());
        //when
        Throwable thrown = catchThrowable(() -> artifactService.findAll());
        //assert
        assertThat(thrown).isInstanceOf(OpjectNotFoundException.class).hasMessage("Could not find any artifact :(");
    }

    @Test
    void testSaveArtifactSuccess()
    {
        //given
        Artifact newArtifact= new Artifact();
        newArtifact.setName("artifact");
        newArtifact.setDescription("desc");
        newArtifact.setImageUrl("url");
        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);
        //when
        Artifact savedArtifact = artifactService.saveArtifact(newArtifact);
        //then
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository,times(1)).save(newArtifact);
    }
    @Test
    void testUpdateArtifactSuccess()
    {
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("16135131");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("any desc for testing");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact updated = new Artifact();
        updated.setId("16135131");
        updated.setName("Invisibility Cloak");
        updated.setDescription("any desc for testing ....... testing update");
        updated.setImageUrl("ImageUrl");
        //given
        given(artifactRepository.findById("16135131")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);
        //when
        Artifact updatedArtifact = artifactService.updateArtifact("16135131", updated);
        //then
        assertThat(updatedArtifact.getId()).isEqualTo(oldArtifact.getId());
        assertThat(updatedArtifact.getName()).isEqualTo(oldArtifact.getName());
        assertThat(updatedArtifact.getDescription()).isEqualTo(updated.getDescription());
        assertThat(updatedArtifact.getImageUrl()).isEqualTo(oldArtifact.getImageUrl());
        verify(artifactRepository,times(1)).findById("16135131");
        verify(artifactRepository,times(1)).save(oldArtifact);
    }
    @Test
    void testUpdateArtifactNotFound()
    {
        Artifact updated = new Artifact();
        updated.setId("16135131");
        updated.setName("Invisibility Cloak");
        updated.setDescription("any desc for testing ....... testing update");
        updated.setImageUrl("ImageUrl");
        //given
        given(artifactRepository.findById("16135131")).willReturn(Optional.empty());
        //when-then
        assertThrows(OpjectNotFoundException.class,()-> artifactService.updateArtifact("16135131",updated));
        verify(artifactRepository,times(1)).findById("16135131");
    }
    @Test
    void deleteArtifactSuccess()
    {
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("16135131");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("any desc for testing");
        oldArtifact.setImageUrl("ImageUrl");

        //given
        given(artifactRepository.findById("16135131")).willReturn(Optional.of(oldArtifact));
        //when
        doNothing().when(artifactRepository).deleteById("16135131");
        //then
        artifactService.deleteArtifact("16135131");
        verify(artifactRepository,times(1)).deleteById("16135131");
    }
    @Test
    void deleteArtifactErrorNotFound()
    {

        //given
        given(artifactRepository.findById("16135131")).willReturn(Optional.empty());
        //when-then
        assertThrows(OpjectNotFoundException.class,()-> artifactService.deleteArtifact("16135131"));
        verify(artifactRepository,times(1)).findById("16135131");
    }
}