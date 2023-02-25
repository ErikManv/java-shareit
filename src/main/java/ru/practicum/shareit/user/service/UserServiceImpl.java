package ru.practicum.shareit.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public List<UserDto> findAll() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user: userStorage.findAll()) {
            usersDto.add(UserMapper.toUserDto(user));
        }
        return usersDto;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
            User user = UserMapper.toUser(userDto);
            emailIsUniq(user, userDto.getId());
            log.info("пользователь {} добавлен", userDto.getName());
            return UserMapper.toUserDto(userStorage.createUser(user));
    }

    @Override
    public UserDto getUser(Integer userId) {
        containsUser(userId);
        return UserMapper.toUserDto(userStorage.getUser(userId));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
            containsUser(userId);
            User user = UserMapper.toUser(userDto);
            emailIsUniq(user, userId);
            log.info("пользователь {} обновлен", user.getName());
            return UserMapper.toUserDto(userStorage.updateUser(user, userId));
    }

    @Override
    public void delete(Integer userId) {
        userStorage.deleteUser(userId);
    }

    private void emailIsUniq(User user, Integer userId) {
        for (User user1: userStorage.findAll()) {
            if (user1.getEmail().equals(user.getEmail()) && !user1.getId().equals(userId)) {
                log.error("{} - данный email уже существует", user.getEmail());
                throw new IllegalArgumentException("такой email уже существует");
            }
        }
    }

    private void containsUser(Integer userId) {
        if (userStorage.getUser(userId) == null) {
            log.error("пользователя с id {} не существует", userId);
            throw new NullPointerException();
        }
    }
}
