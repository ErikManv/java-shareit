package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void addItemRequest() {
        Integer userId = 1;
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .requesterId(1)
            .description("description")
            .created(LocalDateTime.now())
            .build();
        when(itemRequestService.addItemRequest(itemRequestDto, userId)).thenReturn(itemRequestDto);

        String result = mockMvc.perform(post("/requests")
                .header("X-Sharer-User-Id", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itemRequestDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }

    @SneakyThrows
    @Test
    void addItemRequest_whenItemRequestIsNotValid_thenStatusIsBadRequest() {
        Integer userId = 1;
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .requesterId(1)
            .created(LocalDateTime.now())
            .build();
        when(itemRequestService.addItemRequest(itemRequestDto, userId)).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                .header("X-Sharer-User-Id", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itemRequestDto)))
            .andExpect(status().isBadRequest())
            .andDo(print());

        verify(itemRequestService, never()).addItemRequest(itemRequestDto, userId);
    }

    @SneakyThrows
    @Test
    void getAllOwnItemRequests() {
        Integer userId = 1;
        mockMvc.perform(get("/requests").header("X-Sharer-User-Id", userId))
            .andDo(print())
            .andExpect(status().isOk());

        verify(itemRequestService).getOwnRequests(userId);
    }

    @SneakyThrows
    @Test
    void getAllItemRequestsOfUsers() {
        Integer userId = 1;
        Integer offset = 0;
        Integer limit = 10;
        mockMvc.perform(get("/requests/all").header("X-Sharer-User-Id", userId).param("from", "0").param("size", "10"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(itemRequestService).getAllOthersRequests(userId, offset, limit);
    }

    @SneakyThrows
    @Test
    void getItemRequestById() {
        Integer userId = 1;
        Integer requestId = 0;

        mockMvc.perform(get("/requests/{requestId}", requestId)
                .header("X-Sharer-User-Id", userId))
            .andDo(print())
            .andExpect(status().isOk());

        verify(itemRequestService).getRequestById(requestId, userId);
    }
}