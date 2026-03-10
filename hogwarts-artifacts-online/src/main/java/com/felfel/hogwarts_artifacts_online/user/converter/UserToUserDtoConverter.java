package com.felfel.hogwarts_artifacts_online.user.converter;

import com.felfel.hogwarts_artifacts_online.user.User;
import com.felfel.hogwarts_artifacts_online.user.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The type User to user dto converter.
 */
@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User source) {
        return new UserDto(source.getId(),
                source.getUsername(),
                source.getEnabled(),
                source.getRoles());
    }
}
