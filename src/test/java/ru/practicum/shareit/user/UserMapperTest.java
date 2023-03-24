package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapperImpl;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapperImpl userMapper;
    User testUser = User.builder()
        .id(1)
        .name("test")
        .email("test@email.ru")
        .build();

    UserDto testUserDto = UserDto.builder()
        .id(1)
        .name("test")
        .email("test@email.ru")
        .build();


    @Test
    void toDtoUserNotNullTest() {
        UserDto actualUserDto = userMapper.toUserDto(testUser);
        assertEquals(testUserDto.getId(), actualUserDto.getId());
        assertEquals(testUserDto.getName(), actualUserDto.getName());
        assertEquals(testUserDto.getEmail(), actualUserDto.getEmail());
    }

    @Test
    void toDtoUserNullTest() {
        UserDto actualUserDto = userMapper.toUserDto(null);
        assertNull(actualUserDto);
    }

    @Test
    void toUserUserDtoNotNullTest() {
        User actualUser = userMapper.toUser(testUserDto);
        assertEquals(testUser.getId(), actualUser.getId());
        assertEquals(testUser.getName(), actualUser.getName());
        assertEquals(testUser.getEmail(), actualUser.getEmail());
    }

    @Test
    void toUserUserDtoNullTest() {
        User actualUser = userMapper.toUser(null);
        assertNull(actualUser);
    }
}