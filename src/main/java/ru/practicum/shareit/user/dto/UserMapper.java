package ru.practicum.shareit.user.dto;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @InheritInverseConfiguration
    User toUser(UserDto userDto);
}
