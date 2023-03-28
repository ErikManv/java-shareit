package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @InjectMocks
    UserServiceImpl userService;

    User userTest2 = User.builder()
        .email("email@email.ru")
        .name("testUser")
        .id(1)
        .build();

    @Test
    void createUserCorrectTest() {
        UserDto userTest = UserDto.builder()
            .email("email@email.ru")
            .name("testUser")
            .id(2)
            .build();

        when(userMapper.toUser(userTest)).thenReturn(userTest2);

        userService.createUser(userTest);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(userTest.getName(), savedUser.getName());
        assertEquals(userTest.getEmail(), savedUser.getEmail());
    }

    @Test
    void getUserByIdUserNotFoundThrowUserUserNotFoundException() {
        Integer userId = 0;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
            () -> userService.getUserById(userId));
    }

    @Test
    void getUserByIdUserFoundTest() {
        Integer userId = 0;

        UserDto userDtoTest = UserDto.builder()
            .email("email@email.ru")
            .name("testUser")
            .id(1)
            .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(userTest2));
        when(userMapper.toUserDto(any())).thenReturn(userDtoTest);

        assertEquals(userTest2.getId(), userService.getUserById(userId).getId());
    }

    @Test
    void updateUserUserNotFound() {
        Integer userId = 2;
        UserDto testUserDto = UserDto.builder()
            .email("email@email.ru")
            .name("testUser")
            .build();
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(testUserDto, userId));
    }

    @Test
    void updateUserCorrect() {
        Integer userId = 1;
        User testUser = User.builder()
            .id(1)
            .email("email@email.ru")
            .name("testUser")
            .build();
        UserDto testUserDto = UserDto.builder()
            .email("email@email.ru")
            .name("testUser1")
            .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        userService.updateUser(testUserDto, userId);

        verify(userMapper).toUserDto(userArgumentCaptor.capture());
        User updatedUser = userArgumentCaptor.getValue();

        assertEquals(testUserDto.getName(), updatedUser.getName());
    }

    @Test
    void deleteUserTest() {
        Integer userId = 0;

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}