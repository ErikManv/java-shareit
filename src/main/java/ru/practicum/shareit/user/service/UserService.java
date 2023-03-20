package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

     List<UserDto> findAll();

     UserDto createUser(UserDto user);

     UserDto getUserById(Integer userId);

     UserDto updateUser(UserDto user, Integer userId);

     void delete(Integer userId);

}
