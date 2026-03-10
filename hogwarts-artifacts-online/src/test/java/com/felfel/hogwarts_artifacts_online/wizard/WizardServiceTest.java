package com.felfel.hogwarts_artifacts_online.wizard;

import com.felfel.hogwarts_artifacts_online.artifact.Artifact;
import com.felfel.hogwarts_artifacts_online.artifact.ArtifactRepository;
import com.felfel.hogwarts_artifacts_online.system.exception.OpjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * The type Wizard service test.
 */
@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
    /**
     * The Wizard repository.
     */
    @Mock
    WizardRepository wizardRepository;

    /**
     * The Artifact repository.
     */
    @Mock
    ArtifactRepository artifactRepository;

    /**
     * The Wizard service.
     */
    @InjectMocks
    WizardService wizardService;


    /**
     * The Wizard list.
     */
    List<Wizard> wizardList = new ArrayList<>();

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        Wizard w1 =new Wizard();
        w1.setName("ahmed");
        w1.setId(1);

        Wizard w2 =new Wizard();
        w2.setId(2);
        w2.setName("felfel");

        wizardList.addAll(List.of(w1,w2));
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
    }

    /**
     * Find all success.
     */
    @Test
    void findAllSuccess() {
        //given
        given(wizardRepository.findAll()).willReturn(this.wizardList);
        //when
        List<Wizard> returnedWizards = wizardService.findAll();
        //then
        assertThat(returnedWizards).isEqualTo(this.wizardList);
        verify(wizardRepository,times(1)).findAll();
    }

    /**
     * Find all failed.
     */
    @Test
    void findAllFailed() {
        //given
        given(wizardRepository.findAll()).willReturn(new ArrayList<>());
        //when
        Throwable thrown = catchThrowable(()->wizardService.findAll());
        //then
        assertThat(thrown).isInstanceOf(OpjectNotFoundException.class)
                .hasMessage("Could not find any wizard :(");

    }

    /**
     * Save wizard.
     */
    @Test
    void saveWizard() {
        Wizard newWizard = new Wizard();
        newWizard.setName("ahmed");

            Wizard w = new Wizard();
            w.setName("ahmed");
            w.setId(1);
            //given
            given(wizardRepository.save(any(Wizard.class))).willReturn(w);
            //when
            Wizard savedWizard = wizardService.saveWizard(newWizard);
            //then
            assertThat(savedWizard.getName()).isEqualTo(w.getName());
            assertThat(savedWizard).isNotNull();
            verify(wizardRepository,times(1)).save(any(Wizard.class));
    }

    /**
     * Find by id success.
     */
    @Test
    void findByIdSuccess() {
        Wizard w1 =new Wizard();
        w1.setName("ahmed");
        w1.setId(1);
        //given
        given(wizardRepository.findById(1)).willReturn(Optional.of(w1));
        //when
        Wizard foundWizard = wizardService.findById(1);
        //then
        assertThat(foundWizard.getId()).isEqualTo(w1.getId());
        assertThat(foundWizard.getName()).isEqualTo(w1.getName());
        verify(wizardRepository,times(1)).findById(1);
    }

    /**
     * Find by id failed.
     */
    @Test
    void findByIdFailed() {
        //given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());
        //when
        Throwable thrown = catchThrowable(()->wizardService.findById(1));
        //then
        assertThat(thrown).isInstanceOf(OpjectNotFoundException.class)
                .hasMessage("Could not find wizard with ID 1 :(");
    }

    /**
     * Update wizard success.
     */
    @Test
    void updateWizardSuccess() {
        Wizard oldWizard= new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("felfel");

        Wizard updatedWizard= new Wizard();
        updatedWizard.setId(1);
        updatedWizard.setName("ahmed");

        //given
        given(wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(any(Wizard.class))).willReturn(updatedWizard);
        //when
        Wizard result = wizardService.updateWizard(1, updatedWizard);
        //then
        assertThat(result.getId()).isEqualTo(updatedWizard.getId());
        assertThat(result.getName()).isEqualTo(updatedWizard.getName());
        verify(wizardRepository,times(1)).findById(1);
        verify(wizardRepository,times(1)).save(any(Wizard.class));

    }

    /**
     * Update wizard failed.
     */
    @Test
    void updateWizardFailed() {
        Wizard updatedWizard= new Wizard();
        updatedWizard.setId(1);
        updatedWizard.setName("ahmed");
        //given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());
        //when-then
        assertThrows(OpjectNotFoundException.class,()-> wizardService.updateWizard(1,updatedWizard));
        verify(wizardRepository,times(1)).findById(1);
    }

    /**
     * Delete wizard success.
     */
    @Test
    void deleteWizardSuccess() {
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("felfel");
        //given
        given(wizardRepository.findById(1)).willReturn(Optional.of(w));
        //when
        doNothing().when(wizardRepository).deleteById(1);
        //then
        wizardService.deleteWizard(1);
        verify(wizardRepository,times(1)).deleteById(1);
    }

    /**
     * Delete wizard failed.
     */
    @Test
    void deleteWizardFailed() {
        //given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());
        //when-then
        assertThrows(OpjectNotFoundException.class,()-> wizardService.deleteWizard(1));
        verify(wizardRepository,times(1)).findById(1);
    }

    /**
     * Assign artifact success.
     */
    @Test
    void assignArtifactSuccess()
    {
        //given
        Artifact a1 =new Artifact();
        a1.setId("1234567890ABCDEF");
        a1.setName("Deluminator");
        a1.setDescription("A device used to absorb light from a place and then release it.");
        a1.setImageUrl("imageUrl");

        Wizard w1 = new Wizard();
        w1.setName("ahmed");
        w1.setId(1);
        w1.addArtifact(a1);

        Wizard w2 = new Wizard();
        w2.setName("felfel");
        w2.setId(2);

        given(artifactRepository.findById("1234567890ABCDEF")).willReturn(Optional.of(a1));
        given(wizardRepository.findById(2)).willReturn(Optional.of(w2));
        //when
        wizardService.assignArtifact(2,"1234567890ABCDEF");
        //then
        assertThat(a1.getOwner().getId()).isEqualTo(w2.getId());
        assertThat(w2.getArtifacts().contains(a1));
        verify(artifactRepository,times(1)).findById("1234567890ABCDEF");
        verify(wizardRepository,times(1)).findById(2);
    }

    /**
     * Assign artifact error artifact not found.
     */
    @Test
    void assignArtifactErrorArtifactNotFound()
    {

        //given
        given(artifactRepository.findById("1234567890ABCDEF")).willReturn(Optional.empty());
        //when
        Throwable thrown = catchThrowable(()->wizardService.assignArtifact(2,"1234567890ABCDEF"));
        //then
        assertThat(thrown).isInstanceOf(OpjectNotFoundException.class)
                .hasMessage("Could not find artifact with ID 1234567890ABCDEF :(");
        verify(artifactRepository,times(1)).findById("1234567890ABCDEF");
    }

    /**
     * Assign artifact error wizard not found.
     */
    @Test
    void assignArtifactErrorWizardNotFound()
    {
        //given
        Artifact a1 =new Artifact();
        a1.setId("1234567890ABCDEF");
        a1.setName("Deluminator");
        a1.setDescription("A device used to absorb light from a place and then release it.");
        a1.setImageUrl("imageUrl");

        Wizard w1 = new Wizard();
        w1.setName("ahmed");
        w1.setId(1);
        w1.addArtifact(a1);

        given(artifactRepository.findById("1234567890ABCDEF")).willReturn(Optional.of(a1));
        given(wizardRepository.findById(2)).willReturn(Optional.empty());
        //when
        Throwable thrown = catchThrowable(()->wizardService.assignArtifact(2,"1234567890ABCDEF"));
        //then
        assertThat(thrown).isInstanceOf(OpjectNotFoundException.class)
                .hasMessage("Could not find wizard with ID 2 :(");
        verify(wizardRepository,times(1)).findById(2);
    }

}