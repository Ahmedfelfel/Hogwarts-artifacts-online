package com.felfel.hogwarts_artifacts_online.security;

import com.felfel.hogwarts_artifacts_online.user.User;
import com.felfel.hogwarts_artifacts_online.user.UserPrincipals;
import com.felfel.hogwarts_artifacts_online.user.converter.UserToUserDtoConverter;
import com.felfel.hogwarts_artifacts_online.user.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    public Map<String, Object> loginInfo(Authentication authentication) {
        UserPrincipals principals =(UserPrincipals) authentication.getPrincipal();
        User user = principals.getUser();
        UserDto userDto = userToUserDtoConverter.convert(user);
        String token = jwtProvider.createToken(authentication);
        return Map.of("userInfo", userDto, "token", token);

    }
}
