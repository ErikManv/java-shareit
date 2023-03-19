package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final UserMapper userMapper;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public List<UserDto> findAll() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user: repository.findAll()) {
            usersDto.add(userMapper.toUserDto(user));
        }
        return usersDto;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
            User user = userMapper.toUser(userDto);
            log.info("пользователь {} добавлен", userDto.getName());
            return userMapper.toUserDto(repository.save(user));
    }



    @Override
    public UserDto getUserById(Integer userId) {
        return userMapper.toUserDto(getUser(userId));
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, Integer userId) {
            User user = getUser(userId);
            if (userDto.getName() != null) {
            user.setName(userDto.getName());
            }
            if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
            }
            log.info("пользователь {} обновлен", user.getName());
            repository.save(user);
            return userMapper.toUserDto(user);
    }

    @Override
    public void delete(Integer userId) {
        repository.deleteById(userId);
    }

    private User getUser(Integer userId) {
        return repository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("cf"));
    }
}
