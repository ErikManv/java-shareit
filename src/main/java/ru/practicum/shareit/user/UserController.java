package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.markers.Update;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    public UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public List<UserDto> findAll(){
        return userServiceImpl.findAll();
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto){
        return userServiceImpl.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @Validated({Update.class}) @PathVariable Integer id) {
        return userServiceImpl.updateUser(userDto, id);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Integer userId) {
        return userServiceImpl.getUser(userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer userId) {
         userServiceImpl.delete(userId);
    }
}
