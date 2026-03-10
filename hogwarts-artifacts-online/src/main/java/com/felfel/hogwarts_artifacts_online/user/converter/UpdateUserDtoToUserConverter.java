package com.felfel.hogwarts_artifacts_online.user.converter;

import com.felfel.hogwarts_artifacts_online.user.User;
import com.felfel.hogwarts_artifacts_online.user.dto.UpdateUserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The type Update user dto to user converter.
 */
@Component
public class UpdateUserDtoToUserConverter implements Converter<UpdateUserDto, User> {
    @Override
    public User convert(UpdateUserDto source) {
        return new User(null,
                source.username(),
                source.enabled(),
                source.roles(),
                null);
    }
}
