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

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class UserController {

    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final AddUserDtoToUserConverter addUserDtoToUserConverter;
    private final UpdateUserDtoToUserConverter updateUserDtoToUserConverter;

    public UserController(UserService userService,
                          UserToUserDtoConverter userToUserDtoConverter,
                          AddUserDtoToUserConverter addUserDtoToUserConverter,
                          UpdateUserDtoToUserConverter updateUserDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.addUserDtoToUserConverter = addUserDtoToUserConverter;
        this.updateUserDtoToUserConverter = updateUserDtoToUserConverter;
    }

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

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Result findUserById(@PathVariable Integer userId)
    {
        User founduser = this.userService.findUserById(userId);
        UserDto userDto = this.userToUserDtoConverter.convert(founduser);
        return new Result(true,HttpStatus.OK.value(),"find one success",userDto);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Result addUser(@Valid  @RequestBody AddUserDto addUserDto)
    {
        User recivedUser = addUserDtoToUserConverter.convert(addUserDto);
        User savedUser = userService.saveUser(recivedUser);
        UserDto userDto = userToUserDtoConverter.convert(savedUser);
        return new Result(true,HttpStatus.CREATED.value(),"add success",userDto);
    }
    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Result updateUser(@PathVariable Integer userId,@Valid @RequestBody UpdateUserDto updateUserDto)
    {
        User recivedUser = updateUserDtoToUserConverter.convert(updateUserDto);
        User updatedUser = userService.updateUser(userId,recivedUser);
        UserDto userDto = userToUserDtoConverter.convert(updatedUser);
        return new Result(true,HttpStatus.OK.value(),"update success",userDto);
    }
    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Result deleteUserById(@PathVariable Integer userId)
    {
        this.userService.deleteUserById(userId);
        return new Result(true,HttpStatus.OK.value(),"delete success");
    }

}
