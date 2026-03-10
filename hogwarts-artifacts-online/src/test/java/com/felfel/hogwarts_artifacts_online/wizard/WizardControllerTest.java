package com.felfel.hogwarts_artifacts_online.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felfel.hogwarts_artifacts_online.system.exception.OpjectNotFoundException;
import com.felfel.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * The type Wizard controller test.
 */
@WebMvcTest(WizardController.class)
@AutoConfigureMockMvc(addFilters = false)
class WizardControllerTest {

    /**
     * The Wizard service.
     */
    @MockitoBean
    WizardService wizardService;

    /**
     * The Object mapper.
     */
    @Autowired
    ObjectMapper objectMapper;


    /**
     * The Mock mvc.
     */
    @Autowired
    MockMvc mockMvc;
    // define object type for exception message
    private final String OBJECT_TYPE="wizard";

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

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
     * Find all wizards success.
     *
     * @throws Exception the exception
     */
    @Test
    void findAllWizardsSuccess() throws Exception {
        //given
        given(this.wizardService.findAll()).willReturn(wizardList);
        //when-then
        this.mockMvc.perform(get(this.baseUrl+"/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find all success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(wizardList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].name").value(wizardList.get(0).getName()))
                .andExpect(jsonPath("$.data[1].id").value(wizardList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].name").value(wizardList.get(1).getName()));
    }

    /**
     * Find all wizards failed.
     *
     * @throws Exception the exception
     */
    @Test
    void findAllWizardsFailed() throws Exception {
        //given
        given(this.wizardService.findAll()).willThrow(new OpjectNotFoundException(OBJECT_TYPE));
        //when-then
        this.mockMvc.perform(get(this.baseUrl+"/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find any wizard :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Find wizard by id success.
     *
     * @throws Exception the exception
     */
    @Test
    void findWizardByIdSuccess() throws Exception {
        //given
        given(this.wizardService.findById(1)).willReturn(this.wizardList.getFirst());
        //when-then
        this.mockMvc.perform(get(this.baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find one success"))
                .andExpect(jsonPath("$.data.id").value(this.wizardList.getFirst().getId()))
                .andExpect(jsonPath("$.data.name").value(this.wizardList.getFirst().getName()));

    }

    /**
     * Find wizard by id failed.
     *
     * @throws Exception the exception
     */
    @Test
    void findWizardByIdFailed() throws Exception {
        //given
        given(this.wizardService.findById(1)).willThrow(new OpjectNotFoundException(OBJECT_TYPE,1));
        //when-then
        this.mockMvc.perform(get(this.baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    /**
     * Add wizard success.
     *
     * @throws Exception the exception
     */
    @Test
    void addWizardSuccess() throws Exception {
        WizardDto wizardDto=new WizardDto(null,"name",null);
        String json = objectMapper.writeValueAsString(wizardDto);

        Wizard saved =new Wizard();
        saved.setId(1);
        saved.setName("name");

        //given
        given(this.wizardService.saveWizard(Mockito.any(Wizard.class))).willReturn(saved);
        //when-then
        this.mockMvc.perform(post(this.baseUrl+"/wizards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("add success"))
                .andExpect(jsonPath("$.data.id").value(saved.getId()))
                .andExpect(jsonPath("$.data.name").value(saved.getName()));

    }

    /**
     * Update wizard success.
     *
     * @throws Exception the exception
     */
    @Test
    void updateWizardSuccess() throws Exception {
        WizardDto wizardDto=new WizardDto(1,
                                        "name",
                                        null);
        String json = objectMapper.writeValueAsString(wizardDto);
        Wizard updated =new Wizard();
        updated.setId(1);
        updated.setName("wizardName");
        //given
        given(this.wizardService.updateWizard(eq(1),Mockito.any(Wizard.class)))
                .willReturn(updated);
        //when-then
        this.mockMvc.perform(put(this.baseUrl+"/wizards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("update success"))
                .andExpect(jsonPath("$.data.id").value(updated.getId()))
                .andExpect(jsonPath("$.data.name").value(updated.getName()));

    }

    /**
     * Update wizard error id not found.
     *
     * @throws Exception the exception
     */
    @Test
    void updateWizardErrorIdNotFound() throws Exception {
        WizardDto wizardDto=new WizardDto(1,
                "name",
                null);
        String json = objectMapper.writeValueAsString(wizardDto);
        //given
        given(this.wizardService.updateWizard(eq(1),Mockito.any(Wizard.class)))
                .willThrow(new OpjectNotFoundException(OBJECT_TYPE,1));
        //when-then
        this.mockMvc.perform(put(this.baseUrl+"/wizards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Delete wizard success.
     *
     * @throws Exception the exception
     */
    @Test
    void deleteWizardSuccess() throws Exception {
        //given
        doNothing().when(wizardService).deleteWizard(1);
        //when-then
        this.mockMvc.perform(delete(this.baseUrl+"/wizards/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("delete success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Delete wizard error id not found.
     *
     * @throws Exception the exception
     */
    @Test
    void deleteWizardErrorIdNotFound() throws Exception {
        //given
        doThrow(new OpjectNotFoundException(OBJECT_TYPE,1))
                .when(wizardService).deleteWizard(1);
        //when-then
        this.mockMvc.perform(delete(this.baseUrl+"/wizards/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Assign artifact success.
     *
     * @throws Exception the exception
     */
    @Test
    void assignArtifactSuccess() throws Exception {
        //given
        doNothing().when(wizardService).assignArtifact(1,"1234567890ABCDEF");
        //when-then
        this.mockMvc.perform(put(this.baseUrl+"/wizards/1/artifacts/1234567890ABCDEF")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("assign artifact success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Assign artifact error wizard id not found.
     *
     * @throws Exception the exception
     */
    @Test
    void assignArtifactErrorWizardIdNotFound() throws Exception {
        //given
        doThrow(new OpjectNotFoundException(OBJECT_TYPE,1))
                .when(wizardService).assignArtifact(1,"1234567890ABCDEF");
        //when-then
        this.mockMvc.perform(put(this.baseUrl+"/wizards/1/artifacts/1234567890ABCDEF")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Assign artifact error artifact id not found.
     *
     * @throws Exception the exception
     */
    @Test
    void assignArtifactErrorArtifactIdNotFound() throws Exception {
        //given
        doThrow(new OpjectNotFoundException("artifact","1234567890ABCDEF"))
                .when(wizardService).assignArtifact(1,"1234567890ABCDEF");
        //when-then
        this.mockMvc.perform(put(this.baseUrl+"/wizards/1/artifacts/1234567890ABCDEF")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID 1234567890ABCDEF :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}