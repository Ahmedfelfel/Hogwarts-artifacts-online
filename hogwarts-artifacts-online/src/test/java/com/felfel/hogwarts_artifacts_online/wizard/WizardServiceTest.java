package com.felfel.hogwarts_artifacts_online.wizard;

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

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
    @Mock
    WizardRepository wizardRepository;

    @InjectMocks
    WizardService wizardService;


    List<Wizard> wizardList = new ArrayList<>();

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

    @AfterEach
    void tearDown() {
    }

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
    @Test
    void findAllFailed() {
        //given
        given(wizardRepository.findAll()).willReturn(new ArrayList<>());
        //when
        Throwable thrown = catchThrowable(()->wizardService.findAll());
        //then
        assertThat(thrown).isInstanceOf(WizardNotFoundException.class)
                .hasMessage("Could not find wizards :(");

    }

    @Test
    void saveWizard() {
        String wizardName = "felfel";
            Wizard w = new Wizard();
            w.setName(wizardName);
            w.setId(1);
            //given
            given(wizardRepository.save(any(Wizard.class))).willReturn(w);
            //when
            Wizard savedWizard = wizardService.saveWizard(wizardName);
            //then
            assertThat(savedWizard.getName()).isEqualTo(w.getName());
            assertThat(savedWizard).isNotNull();
            verify(wizardRepository,times(1)).save(any(Wizard.class));
    }

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
    @Test
    void findByIdFailed() {
        //given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());
        //when
        Throwable thrown = catchThrowable(()->wizardService.findById(1));
        //then
        assertThat(thrown).isInstanceOf(WizardNotFoundException.class)
                .hasMessage("Could not find wizard with ID 1 :(");
    }

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
        Wizard result = wizardService.updateWizard(1, "ahmed");
        //then
        assertThat(result.getId()).isEqualTo(updatedWizard.getId());
        assertThat(result.getName()).isEqualTo(updatedWizard.getName());
        verify(wizardRepository,times(1)).findById(1);
        verify(wizardRepository,times(1)).save(any(Wizard.class));

    }
    @Test
    void updateWizardFailed() {
        String updatedWizardName= "felfel";
        //given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());
        //when-then
        assertThrows(WizardNotFoundException.class,()->{
            wizardService.updateWizard(1,updatedWizardName);
        });
        verify(wizardRepository,times(1)).findById(1);
    }

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
    @Test
    void deleteWizardFailed() {
        //given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());
        //when-then
        assertThrows(WizardNotFoundException.class,()->{
            wizardService.deleteWizard(1);
        });
        verify(wizardRepository,times(1)).findById(1);
    }
}