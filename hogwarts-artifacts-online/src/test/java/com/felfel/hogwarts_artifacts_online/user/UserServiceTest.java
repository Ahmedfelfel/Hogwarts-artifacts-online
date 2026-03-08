package com.felfel.hogwarts_artifacts_online.user;

import com.felfel.hogwarts_artifacts_online.system.exception.OpjectNotFoundException;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
    void findAllSuccess() {
        //given
        given(userRepository.findAll()).willReturn(userList);
        //when
        List<User> returnedUsers = this.userService.findAll();
        //then
        assertThat(returnedUsers.size()).isEqualTo(userList.size());
        assertThat(returnedUsers.getFirst().getId()).isEqualTo(userList.getFirst().getId());
        assertThat(returnedUsers.getFirst().getUsername()).isEqualTo(userList.getFirst().getUsername());
        assertThat(returnedUsers.getLast().getId()).isEqualTo(userList.getLast().getId());
        assertThat(returnedUsers.getLast().getUsername()).isEqualTo(userList.getLast().getUsername());

    }
    @Test
    void findAllFailed() {
        //given
        given(userRepository.findAll()).willReturn(new ArrayList<>());
        //when
        Throwable thrown = catchThrowable(()->userService.findAll());
        //then
        assertThat(thrown).isInstanceOf(OpjectNotFoundException.class)
                .hasMessage("Could not find any user :(");
    }

    @Test
    void findUserByIdSuccess() {
        //given
        given(userRepository.findById(userList.getFirst().getId()))
                .willReturn(Optional.of(userList.getFirst()));
        //when
        User returenedUser = userService.findUserById(userList.getFirst().getId());
        //then
        assertThat(returenedUser.getId()).isEqualTo(userList.getFirst().getId());
        assertThat(returenedUser.getUsername()).isEqualTo(userList.getFirst().getUsername());
        assertThat(returenedUser.getEnabled()).isEqualTo(userList.getFirst().getEnabled());
        assertThat(returenedUser.getRoles()).isEqualTo(userList.getFirst().getRoles());
        verify(userRepository,times(1)).findById(userList.getFirst().getId());
    }
    @Test
    void findUserByIdErrorUserNotFound() {
        //given
        given(this.userRepository.findById(5)).willReturn(Optional.empty());
        //when-then
        assertThrows(OpjectNotFoundException.class,()-> userService.findUserById(5));
        verify(userRepository,times(1)).findById(5);
    }

    @Test
    void saveUserSuccess() {
        //given
        User savedUser =  new User( 4,
                                    "jack",
                                    true,
                                    "user",
                                    "123");
        User addedUser =  new User();
        addedUser.setUsername("jack");
        addedUser.setEnabled(true);
        addedUser.setRoles("user");
        addedUser.setPassword("123");
        given(userRepository.save(addedUser)).willReturn(savedUser);
        //when
        User returenedUser = userService.saveUser(addedUser);
        //then
        assertThat(returenedUser.getId()).isEqualTo(savedUser.getId());
        assertThat(returenedUser.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(returenedUser.getEnabled()).isEqualTo(savedUser.getEnabled());
        assertThat(returenedUser.getRoles()).isEqualTo(savedUser.getRoles());
        assertThat(returenedUser.getPassword()).isEqualTo(savedUser.getPassword());
        verify(this.userRepository,times(1)).save(addedUser);
    }
//    @Test
//    void addUserErrorUserNotFound() {
//        //given
//        given(this.userRepository.findById(5)).willReturn(Optional.empty());
//        //when-then
//        assertThrows(OpjectNotFoundException.class,()-> userService.findUserById(5));
//        verify(userRepository,times(1)).findById(5);
//    }

    @Test
    void updateUserSuccess() {
        //given
        User oldUser =  new User( 4,
                "ali",
                true,
                "user",
                "123");

        User update =  new User();
        update.setUsername("Mohammed");
        update.setEnabled(true);
        update.setRoles("user");
        given(this.userRepository.findById(oldUser.getId())).willReturn(Optional.of(oldUser));
        given(this.userRepository.save(oldUser)).willReturn(oldUser);
        //when
        User updatedUser = this.userService.updateUser(4,update);
        //then
        assertThat(updatedUser.getId()).isEqualTo(oldUser.getId());
        assertThat(updatedUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(updatedUser.getEnabled()).isEqualTo(oldUser.getEnabled());
        assertThat(updatedUser.getRoles()).isEqualTo(oldUser.getRoles());
        verify(this.userRepository,times(1)).findById(4);
        verify(this.userRepository,times(1)).save(oldUser);
    }
    @Test
    void updateUserErrorIdNotFound() {
        User oldUser =  new User( 4,
                "ali",
                true,
                "user",
                "123");
        //given
        given(this.userRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
        //when
        assertThrows(OpjectNotFoundException.class,()->{
            userService.updateUser(4,oldUser);
        });
        //then
        verify(this.userRepository,times(1)).findById(4);
    }

    @Test
    void deleteUserByIdSuccess() {
        //given
        User oldUser =  new User( 4,
                "ali",
                true,
                "user",
                "123");
        given(this.userRepository.findById(oldUser.getId())).willReturn(Optional.of(oldUser));
        //when
        doNothing().when(this.userRepository).deleteById(4);
        //then
        userService.deleteUserById(4);
        verify(this.userRepository,times(1)).deleteById(4);
    }
    @Test
    void deleteUserErrorIdNotFound() {
        //given
        given(this.userRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
        //when
        assertThrows(OpjectNotFoundException.class,()->{
            userService.deleteUserById(4);
        });
        //then
        verify(this.userRepository,times(1)).findById(4);
    }
}