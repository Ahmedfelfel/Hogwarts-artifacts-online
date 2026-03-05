package com.felfel.hogwarts_artifacts_online.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felfel.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @MockitoBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

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
    void findArtifactByIdSuccess() throws Exception {
        //given
        given(this.artifactService.findById("1234567890ABCDEF")).willReturn(this.artifactList.getFirst());
        //when
        this.mockMvc.perform(get("/api/v1/artifacts/1234567890ABCDEF").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find one success"))
                .andExpect(jsonPath("$.data.id").value("1234567890ABCDEF"))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));

    }
    @Test
    void findArtifactByIdFailed() throws Exception {
        //given
        given(this.artifactService.findById("1234567890ABCDEF")).willThrow(new ArtifactNotFoundException("1234567890ABCDEF"));
        //when-
        this.mockMvc.perform(get("/api/v1/artifacts/1234567890ABCDEF").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID 1234567890ABCDEF :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }
    @Test
    void findAllSuccess() throws Exception {
        //given
        given(this.artifactService.findAll()).willReturn(this.artifactList);
        //when
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find all success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data",hasSize(this.artifactList.size())))
                .andExpect(jsonPath("$.data[0].id").value("1234567890ABCDEF"))
                .andExpect(jsonPath("$.data[-1].id").value("1234567890ABCDE3"));
    }
    @Test
    void findAllFailed() throws Exception {
        //given
        given(this.artifactService.findAll()).willThrow(new ArtifactNotFoundException());
        //when-Then
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find artifacts :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void addArtifactSuccess() throws Exception {
        //given
        ArtifactDto artifactDto = new ArtifactDto(null,
                                            "artifact",
                                            "desc",
                                            "url",
                                            null);
        String json = objectMapper.writeValueAsString(artifactDto);
        Artifact savedArtifact=new Artifact();
        savedArtifact.setId("123456");
        savedArtifact.setName("artifact");
        savedArtifact.setDescription("desc");
        savedArtifact.setImageUrl("url");
        given(this.artifactService.saveArtifact(Mockito.any(Artifact.class))).willReturn(savedArtifact);
        //when-then
        this.mockMvc.perform(post("/api/v1/artifacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("add success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("artifact"))
                .andExpect(jsonPath("$.data.description").value("desc"))
                .andExpect(jsonPath("$.data.imageUrl").value("url"));
    }
    @Test
    void updateArtifactSuccess() throws Exception {
        //given
        ArtifactDto artifactDto = new ArtifactDto("1234567890ABCDEF",
                "artifact",
                "desc",
                "url",
                null);
        String json = objectMapper.writeValueAsString(artifactDto);
        Artifact updated=new Artifact();
        updated.setId("1234567890ABCDEF");
        updated.setName("artifact");
        updated.setDescription("desc");
        updated.setImageUrl("url");
        given(this.artifactService.updateArtifact(eq("1234567890ABCDEF"),Mockito.any(Artifact.class))).willReturn(updated);
        //when-then
        this.mockMvc.perform(put("/api/v1/artifacts/1234567890ABCDEF")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("update success"))
                .andExpect(jsonPath("$.data.id").value(updated.getId()))
                .andExpect(jsonPath("$.data.name").value(updated.getName()))
                .andExpect(jsonPath("$.data.description").value(updated.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updated.getImageUrl()));
    }
    @Test
    void updateArtifactErrorIdNotFound() throws Exception {
        //given
        ArtifactDto artifactDto = new ArtifactDto("1234567890ABCDEF",
                "artifact",
                "desc",
                "url",
                null);
        String json = objectMapper.writeValueAsString(artifactDto);

        given(this.artifactService.updateArtifact(eq("1234567890ABCDEF"),Mockito.any(Artifact.class)))
                .willThrow(new ArtifactNotFoundException("1234567890ABCDEF"));
        //when-then
        this.mockMvc.perform(put("/api/v1/artifacts/1234567890ABCDEF")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID 1234567890ABCDEF :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteArtifactSuccess() throws Exception {
        //given
        doNothing().when(artifactService).deleteArtifact("1234567890ABCDEF");
        //when-then
        this.mockMvc.perform(delete("/api/v1/artifacts/1234567890ABCDEF")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("delete success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteArtifactErrorIdNotFound() throws Exception {
        //given
       doThrow(new ArtifactNotFoundException("1234567890ABCDEF"))
               .when(artifactService).deleteArtifact("1234567890ABCDEF");
        //when-then
        this.mockMvc.perform(delete("/api/v1/artifacts/1234567890ABCDEF")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID 1234567890ABCDEF :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}