package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void updateUser_UserNotFound() {
        Integer userId = 2;
        UserDto testUserDto = UserDto.builder()
            .email("email@email.ru")
            .name("test User")
            .build();
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(testUserDto, userId));
    }

//    @Test
//    void updateUser_whenDuplicateEmail_thenEmailDuplicateExceptionThrown() {
//        Integer userId = 2;
//        User testUser = User.builder()
//            .id(1)
//            .email("email@email.ru")
//            .name("test User")
//            .build();
//        UserDto testUserDto = UserDto.builder()
//            .email("email@email.ru1")
//            .name("test User")
//            .build();
//        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
//
//        assertThrows(MethodArgumentNotValidException.class, () -> userService.updateUser(testUserDto, userId));
//    }

    @Test
    void updateUserCorrect() {
        Integer userId = 1;
        User testUser = User.builder()
            .id(1)
            .email("email@email.ru")
            .name("test User")
            .build();
        UserDto testUserDto = UserDto.builder()
            .email("email@email.ru")
            .name("test User1")
            .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        userService.updateUser(testUserDto, userId);

        verify(userMapper).toUserDto(userArgumentCaptor.capture());
        User updatedUser = userArgumentCaptor.getValue();

        assertEquals(testUserDto.getName(), updatedUser.getName());
    }
}