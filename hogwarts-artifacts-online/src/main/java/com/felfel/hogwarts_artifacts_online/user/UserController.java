package com.felfel.hogwarts_artifacts_online.user;

import com.felfel.hogwarts_artifacts_online.system.Result;
import com.felfel.hogwarts_artifacts_online.user.converter.AddUserDtoToUserConverter;
import com.felfel.hogwarts_artifacts_online.user.converter.UpdateUserDtoToUserConverter;
import com.felfel.hogwarts_artifacts_online.user.converter.UserToUserDtoConverter;
import com.felfel.hogwarts_artifacts_online.user.dto.AddUserDto;
import com.felfel.hogwarts_artifacts_online.user.dto.UpdateUserDto;
import com.felfel.hogwarts_artifacts_online.user.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("${api.endpoint.base-url}")
public class UserController {

    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final AddUserDtoToUserConverter addUserDtoToUserConverter;
    private final UpdateUserDtoToUserConverter updateUserDtoToUserConverter;

    /**
     * Instantiates a new User controller.
     *
     * @param userService                  the user service
     * @param userToUserDtoConverter       the user to user dto converter
     * @param addUserDtoToUserConverter    the add user dto to user converter
     * @param updateUserDtoToUserConverter the update user dto to user converter
     */
    public UserController(UserService userService,
                          UserToUserDtoConverter userToUserDtoConverter,
                          AddUserDtoToUserConverter addUserDtoToUserConverter,
                          UpdateUserDtoToUserConverter updateUserDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.addUserDtoToUserConverter = addUserDtoToUserConverter;
        this.updateUserDtoToUserConverter = updateUserDtoToUserConverter;
    }

    /**
     * Find all users result.
     *
     * @return the result
     */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Result findAllUsers()
    {
        List<User> users = this.userService.findAll();
        List<UserDto> userDtos = users
                .stream()
                .map(this.userToUserDtoConverter::convert)
                .toList();
        return new Result(true, HttpStatus.OK.value(),"find all success",userDtos);
    }

    /**
     * Find user by id result.
     *
     * @param userId the user id
     * @return the result
     */
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Result findUserById(@PathVariable Integer userId)
    {
        User founduser = this.userService.findUserById(userId);
        UserDto userDto = this.userToUserDtoConverter.convert(founduser);
        return new Result(true,HttpStatus.OK.value(),"find one success",userDto);
    }

    /**
     * Add user result.
     *
     * @param addUserDto the add user dto
     * @return the result
     */
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Result addUser(@Valid  @RequestBody AddUserDto addUserDto)
    {
        User recivedUser = addUserDtoToUserConverter.convert(addUserDto);
        User savedUser = userService.saveUser(recivedUser);
        UserDto userDto = userToUserDtoConverter.convert(savedUser);
        return new Result(true,HttpStatus.CREATED.value(),"add success",userDto);
    }

    /**
     * Update user result.
     *
     * @param userId        the user id
     * @param updateUserDto the update user dto
     * @return the result
     */
    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Result updateUser(@PathVariable Integer userId,@Valid @RequestBody UpdateUserDto updateUserDto)
    {
        User recivedUser = updateUserDtoToUserConverter.convert(updateUserDto);
        User updatedUser = userService.updateUser(userId,recivedUser);
        UserDto userDto = userToUserDtoConverter.convert(updatedUser);
        return new Result(true,HttpStatus.OK.value(),"update success",userDto);
    }

    /**
     * Delete user by id result.
     *
     * @param userId the user id
     * @return the result
     */
    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Result deleteUserById(@PathVariable Integer userId)
    {
        this.userService.deleteUserById(userId);
        return new Result(true,HttpStatus.OK.value(),"delete success");
    }

}
