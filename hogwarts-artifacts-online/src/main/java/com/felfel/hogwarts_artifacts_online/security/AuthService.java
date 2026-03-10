package com.felfel.hogwarts_artifacts_online.security;

import com.felfel.hogwarts_artifacts_online.user.User;
import com.felfel.hogwarts_artifacts_online.user.UserPrincipals;
import com.felfel.hogwarts_artifacts_online.user.converter.UserToUserDtoConverter;
import com.felfel.hogwarts_artifacts_online.user.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * The type Auth service.
 */
@Service
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserToUserDtoConverter userToUserDtoConverter;

    /**
     * Instantiates a new Auth service.
     *
     * @param jwtProvider            the jwt provider
     * @param userToUserDtoConverter the user to user dto converter
     */
    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    /**
     * Login info map.
     *
     * @param authentication the authentication
     * @return the map
     */
    public Map<String, Object> loginInfo(Authentication authentication) {
        UserPrincipals principals =(UserPrincipals) authentication.getPrincipal();
        User user = principals.getUser();
        UserDto userDto = userToUserDtoConverter.convert(user);
        String token = jwtProvider.createToken(authentication);
        return Map.of("userInfo", userDto, "token", token);

    }
}
