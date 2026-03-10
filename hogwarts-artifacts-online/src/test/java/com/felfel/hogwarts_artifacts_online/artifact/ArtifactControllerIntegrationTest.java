package com.felfel.hogwarts_artifacts_online.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felfel.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * The type Artifact controller integration test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration test Artifact endpoint api")
@Tag("Integration")
public class ArtifactControllerIntegrationTest {

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

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    private String authHeader;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
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

    /**
     * Find all success.
     *
     * @throws Exception the exception
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findAllSuccess() throws Exception {
        //when
        this.mockMvc.perform(get(this.baseUrl + "/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find all success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(4)));
    }

    /**
     * Add artifact success.
     *
     * @throws Exception the exception
     */
    @Test
    void addArtifactSuccess() throws Exception {
        //given
        ArtifactDto artifactDto = new ArtifactDto(null,
                "artifact",
                "desc",
                "url",
                null);
        String json = objectMapper.writeValueAsString(artifactDto);

        this.mockMvc.perform(post(this.baseUrl + "/artifacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("add success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("artifact"))
                .andExpect(jsonPath("$.data.description").value("desc"))
                .andExpect(jsonPath("$.data.imageUrl").value("url"));

        //check the new artifact is added

        this.mockMvc.perform(get(this.baseUrl + "/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find all success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(5)));
    }

    /**
     * Update artifact success.
     *
     * @throws Exception the exception
     */
    @Test
    void updateArtifactSuccess() throws Exception {
        //given
        ArtifactDto artifactDto = new ArtifactDto("1234567890ABCDEF",
                "artifact",
                "desc",
                "url",
                null);
        String json = objectMapper.writeValueAsString(artifactDto);
        //when-then
        this.mockMvc.perform(put(this.baseUrl+"/artifacts/1234567890ABCDEF")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization",authHeader)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("update success"))
                .andExpect(jsonPath("$.data.id").value(artifactDto.id()))
                .andExpect(jsonPath("$.data.name").value(artifactDto.name()))
                .andExpect(jsonPath("$.data.description").value(artifactDto.description()))
                .andExpect(jsonPath("$.data.imageUrl").value(artifactDto.imageUrl()));
    }

    /**
     * Delete artifact success.
     *
     * @throws Exception the exception
     */
    @Test
    void deleteArtifactSuccess() throws Exception {

        //when-then
        this.mockMvc.perform(delete(this.baseUrl+"/artifacts/1234567890ABCDE3")
                        .accept(MediaType.APPLICATION_JSON).header("Authorization",authHeader))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("delete success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Find artifact by id success.
     *
     * @throws Exception the exception
     */
    @Test
    void findArtifactByIdSuccess() throws Exception {
        //when
        this.mockMvc.perform(get(this.baseUrl+"/artifacts/1234567890ABCDEF").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find one success"))
                .andExpect(jsonPath("$.data.id").value("1234567890ABCDEF"))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));

    }

    /**
     * Find artifact by id failed.
     *
     * @throws Exception the exception
     */
    @Test
    void findArtifactByIdFailed() throws Exception {
       //when-
        this.mockMvc.perform(get(this.baseUrl+"/artifacts/1234567890ABCDE3").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID 1234567890ABCDE3 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    /**
     * Update artifact error id not found.
     *
     * @throws Exception the exception
     */
    @Test
    void updateArtifactErrorIdNotFound() throws Exception {
        //given
        ArtifactDto artifactDto = new ArtifactDto("1234567890ABCDE5",
                "artifact",
                "desc",
                "url",
                null);
        String json = objectMapper.writeValueAsString(artifactDto);
        //when-then
        this.mockMvc.perform(put(this.baseUrl+"/artifacts/1234567890ABCDE5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization",authHeader)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID 1234567890ABCDE5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Delete artifact error id not found.
     *
     * @throws Exception the exception
     */
    @Test
    void deleteArtifactErrorIdNotFound() throws Exception {
        //when-then
        this.mockMvc.perform(delete(this.baseUrl+"/artifacts/1234567890ABCDE3")
                        .accept(MediaType.APPLICATION_JSON).header("Authorization",authHeader))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID 1234567890ABCDE3 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}