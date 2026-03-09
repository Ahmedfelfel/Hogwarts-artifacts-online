package com.felfel.hogwarts_artifacts_online.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felfel.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration test Wizard endpoint api")
@Tag("Integration")
public class WizardControllerIntegrationTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    private String authHeader;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions result = this.mockMvc
                .perform(post(this.baseUrl + "/users/login")
                        .with(httpBasic("john", "123")));
        MvcResult mvc = result.andDo(print()).andReturn();
        String content = mvc.getResponse().getContentAsString();
        String token = objectMapper.readTree(content).get("data").get("token").asText();

        this.authHeader = "Bearer " + token;
    }
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findAllWizardsSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl+"/wizards").accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find all success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data",hasSize(2)));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findWizardByIdSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl+"/wizards/2").accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find one success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.name").value("felfel"));

    }
    @Test
    void findWizardByIdFailed() throws Exception {

        this.mockMvc.perform(get(this.baseUrl+"/wizards/5").accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void addWizardSuccess() throws Exception {
        WizardDto wizardDto=new WizardDto(null,"name",null);
        String json = objectMapper.writeValueAsString(wizardDto);
        this.mockMvc.perform(post(this.baseUrl+"/wizards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", authHeader)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("add success"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.name").value(wizardDto.name()));
    }

    @Test
    void updateWizardSuccess() throws Exception {
        WizardDto wizardDto=new WizardDto(2,
                "name",
                null);
        String json = objectMapper.writeValueAsString(wizardDto);
        this.mockMvc.perform(put(this.baseUrl+"/wizards/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", authHeader)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("update success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.name").value(wizardDto.name()));

    }
    @Test
    void updateWizardErrorIdNotFound() throws Exception {
        WizardDto wizardDto=new WizardDto(1,
                "name",
                null);
        String json = objectMapper.writeValueAsString(wizardDto);
        this.mockMvc.perform(put(this.baseUrl+"/wizards/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", authHeader)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID 4 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deleteWizardSuccess() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl+"/wizards/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("delete success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteWizardErrorIdNotFound() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl+"/wizards/5")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void assignArtifactSuccess() throws Exception {
        this.mockMvc.perform(put(this.baseUrl+"/wizards/2/artifacts/1234567890ABCDEF")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("assign artifact success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void assignArtifactErrorWizardIdNotFound() throws Exception {
        this.mockMvc.perform(put(this.baseUrl+"/wizards/5/artifacts/1234567890ABCDEF")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void assignArtifactErrorArtifactIdNotFound() throws Exception {
        this.mockMvc.perform(put(this.baseUrl+"/wizards/1/artifacts/1234567890ABCDE9")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID 1234567890ABCDE9 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
