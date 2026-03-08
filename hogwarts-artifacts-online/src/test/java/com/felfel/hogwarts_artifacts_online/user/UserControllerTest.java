package com.felfel.hogwarts_artifacts_online.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felfel.hogwarts_artifacts_online.system.exception.OpjectNotFoundException;
import com.felfel.hogwarts_artifacts_online.user.dto.AddUserDto;
import com.felfel.hogwarts_artifacts_online.user.dto.UpdateUserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SuppressWarnings("FieldMayBeFinal")
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;

    private final String OBJECT_TYPE="user";

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    private List<User> userList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userList.addAll(List.of(
                new User( 1, "john", true, "admin user","123"),
                new User( 2,  "eric", true, "user" ,"123"),
                new User(  3, "tom",  false, "user" ,"123")));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAllUsersSuccess() throws Exception {
        //given
        given(this.userService.findAll()).willReturn(userList);
        //when-then
        mockMvc.perform(get(this.baseUrl+"/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find all success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(userList.getFirst().getId()))
                .andExpect(jsonPath("$.data[-1].id").value(userList.getLast().getId()));
    }
    @Test
    void findAllUsersFailed() throws Exception {
        //given
        given(this.userService.findAll()).willThrow(new OpjectNotFoundException(OBJECT_TYPE));
        //when-then
        mockMvc.perform(get(this.baseUrl+"/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find any user :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void findUserByIdSuccess() throws Exception {
        //given
        given(this.userService.findUserById(userList.getFirst().getId())).willReturn(userList.getFirst());
        //when-then
        mockMvc.perform(get(this.baseUrl+"/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("find one success"))
                .andExpect(jsonPath("$.data.id").value(userList.getFirst().getId()))
                .andExpect(jsonPath("$.data.username").value(userList.getFirst().getUsername()))
                .andExpect(jsonPath("$.data.enabled").value(userList.getFirst().getEnabled()))
                .andExpect(jsonPath("$.data.roles").value(userList.getFirst().getRoles()));
    }
    @Test
    void findUserByIdFailed() throws Exception {
        //given
        given(this.userService.findUserById(4)).willThrow(new OpjectNotFoundException(OBJECT_TYPE,4));
        //when-then
        mockMvc.perform(get(this.baseUrl+"/users/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find user with ID 4 :("))
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
        User savedUser = new User(1,
                                "ahmed",
                                true,
                                "user",
                                "123");
        given(this.userService.saveUser(Mockito.any(User.class))).willReturn(savedUser);
        //when-then
        mockMvc.perform(post(this.baseUrl+"/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("add success"))
                .andExpect(jsonPath("$.data.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.data.username").value(savedUser.getUsername()))
                .andExpect(jsonPath("$.data.enabled").value(savedUser.getEnabled()))
                .andExpect(jsonPath("$.data.roles").value(savedUser.getRoles()));
    }

    @Test
    void updateUserSuccess() throws Exception {
        //given
        UpdateUserDto updateUserDto = new UpdateUserDto("ahmed",
                                                        true,
                                                        "user");
        String json =objectMapper.writeValueAsString(updateUserDto);
        User updatedUser = new User(1,
                "ahmed",
                true,
                "user",
                "123");
        given(this.userService.updateUser(eq(1),Mockito.any(User.class))).willReturn(updatedUser);
        //when-then
        mockMvc.perform(put(this.baseUrl+"/users/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("update success"))
                .andExpect(jsonPath("$.data.id").value(updatedUser.getId()))
                .andExpect(jsonPath("$.data.username").value(updatedUser.getUsername()))
                .andExpect(jsonPath("$.data.enabled").value(updatedUser.getEnabled()))
                .andExpect(jsonPath("$.data.roles").value(updatedUser.getRoles()));
    }
    @Test
    void updateUserErrorIdNotFound() throws Exception {
        //given
        UpdateUserDto updateUserDto = new UpdateUserDto("ahmed",
                                                        true,
                                                        "user");
        String json =objectMapper.writeValueAsString(updateUserDto);
        given(this.userService.updateUser(eq(1),Mockito.any(User.class)))
                .willThrow(new OpjectNotFoundException(OBJECT_TYPE,1));
        //when-then
        mockMvc.perform(put(this.baseUrl+"/users/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find user with ID 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteUserByIdSuccess() throws Exception {
    //given
        doNothing().when(this.userService).deleteUserById(1);
    //when-then
        mockMvc.perform(delete(this.baseUrl+"/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("delete success"))
                .andExpect(jsonPath("$.data").isEmpty());

    }
    @Test
    void deleteUserByIdErrorIdNotFound() throws Exception {
    //given
        doThrow(new OpjectNotFoundException(OBJECT_TYPE,1))
                .when(this.userService).deleteUserById(1);
    //when-then
        mockMvc.perform(delete(this.baseUrl+"/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find user with ID 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }
}