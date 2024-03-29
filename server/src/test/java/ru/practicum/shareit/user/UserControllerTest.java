package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    void addUserValidStatusIsOk() {
        UserDto userDto = UserDto.builder()
            .name("test")
            .email("test@test.ru")
            .build();
        when(userService.createUser(any())).thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void addUserUserIsNotValidBadRequest() {
        UserDto userDtoToAdd = UserDto.builder().build();
        when(userService.createUser(userDtoToAdd)).thenReturn(userDtoToAdd);

        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDtoToAdd)))
            .andExpect(status().isBadRequest())
            .andDo(print());

        verify(userService, never()).createUser(userDtoToAdd);
    }

    @SneakyThrows
    @Test
    void updateUser() {
        Integer userId = 1;
        UserDto userDto = UserDto.builder()
            .name("test")
            .email("test@test.ru")
            .build();
        when(userService.updateUser(any(), any())).thenReturn(userDto);

        String result = mockMvc.perform(patch("/users/{userId}", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void getUserById() {
        Integer userId = 0;

        mockMvc.perform(get("/users/{userId}", userId))
            .andDo(print())
            .andExpect(status().isOk());

        verify(userService).getUserById(userId);
    }

    @SneakyThrows
    @Test
    void getAllUsers() {
        mockMvc.perform(get("/users"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(userService).findAll();
    }

    @SneakyThrows
    @Test
    void deleteUserById() {
        Integer userId = 0;

        mockMvc.perform(delete("/users/{userId}", userId))
            .andDo(print())
            .andExpect(status().isOk());

        verify(userService).delete(userId);
    }
}