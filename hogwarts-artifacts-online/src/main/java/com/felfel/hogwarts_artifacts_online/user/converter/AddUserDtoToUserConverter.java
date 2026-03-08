package com.felfel.hogwarts_artifacts_online.user.converter;

import com.felfel.hogwarts_artifacts_online.user.User;
import com.felfel.hogwarts_artifacts_online.user.dto.AddUserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddUserDtoToUserConverter implements Converter<AddUserDto, User> {
    @Override
    public User convert(AddUserDto source) {
        return new User(null,source.username(),source.enabled(),source.roles(),source.password());
    }
}
