package com.felfel.hogwarts_artifacts_online.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felfel.hogwarts_artifacts_online.user.dto.AddUserDto;
import com.felfel.hogwarts_artifacts_online.user.dto.UpdateUserDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration test User endpoint api")
@Tag("Integration")
public class UserControllerIntegrationTest {
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
    void findAllUsersSuccess() throws Exception {

        mockMvc.perform(get(this.baseUrl+"/users").accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find all success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data",hasSize(3)));
    }
    @Test
    void findUserByIdSuccess() throws Exception {

        mockMvc.perform(get(this.baseUrl+"/users/1").accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find one success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("john"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.roles").value("admin user"));
    }
    @Test
    void findUserByIdFailed() throws Exception {
        mockMvc.perform(get(this.baseUrl+"/users/5").accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find user with ID 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void addUser() throws Exception {
        //given
        AddUserDto addUserDto = new AddUserDto("ahmed",
                "123",
                true,
                "user");
        String json =objectMapper.writeValueAsString(addUserDto);
        //when-then
        mockMvc.perform(post(this.baseUrl+"/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("add success"))
                .andExpect(jsonPath("$.data.id").value(4))
                .andExpect(jsonPath("$.data.username").value(addUserDto.username()))
                .andExpect(jsonPath("$.data.enabled").value(addUserDto.enabled()))
                .andExpect(jsonPath("$.data.roles").value(addUserDto.roles()));
    }

    @Test
    void updateUserSuccess() throws Exception {
        //given
        UpdateUserDto updateUserDto = new UpdateUserDto("ahmed",
                true,
                "user");
        String json =objectMapper.writeValueAsString(updateUserDto);
        //when-then
        mockMvc.perform(put(this.baseUrl+"/users/2")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("update success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.username").value(updateUserDto.username()))
                .andExpect(jsonPath("$.data.enabled").value(updateUserDto.enabled()))
                .andExpect(jsonPath("$.data.roles").value(updateUserDto.roles()));
    }
    @Test
    void updateUserErrorIdNotFound() throws Exception {
        //given
        UpdateUserDto updateUserDto = new UpdateUserDto("ahmed",
                true,
                "user");
        String json =objectMapper.writeValueAsString(updateUserDto);
        //when-then
        mockMvc.perform(put(this.baseUrl+"/users/5")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find user with ID 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteUserByIdSuccess() throws Exception {
        mockMvc.perform(delete(this.baseUrl+"/users/3")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("delete success"))
                .andExpect(jsonPath("$.data").isEmpty());

    }
    @Test
    void deleteUserByIdErrorIdNotFound() throws Exception {
        //when-then
        mockMvc.perform(delete(this.baseUrl+"/users/3")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find user with ID 3 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }
}
