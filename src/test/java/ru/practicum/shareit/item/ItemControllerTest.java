package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    @SneakyThrows
    @Test
    void addItem() {
        Integer userId = 1;
        ItemDto itemDto = ItemDto.builder()
            .name("test item")
            .description("test description")
            .available(true)
            .build();
        when(itemService.addItem(any(), any())).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itemDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void addItemItemIsNotValidBadRequest() {
        Integer userId = 1;
        ItemDto itemDto = ItemDto.builder()
            .name("")
            .description("test description")
            .available(true)
            .build();
        when(itemService.addItem(itemDto, userId)).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itemDto)))
            .andExpect(status().isBadRequest())
            .andDo(print());

        verify(itemService, never()).addItem(itemDto, userId);
    }

    @SneakyThrows
    @Test
    void updateItem() {
        Integer itemId = 1;
        Integer userId = 1;
        ItemDto itemDto = ItemDto.builder()
            .name("test item")
            .description("test description")
            .available(true)
            .build();
        when(itemService.updateItem(any(), any(), any())).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itemDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void getItemById() {
        Integer userId = 1;
        Integer itemId = 0;

        mockMvc.perform(get("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", userId))
            .andDo(print())
            .andExpect(status().isOk());

        verify(itemService).getItemById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void personalItemsTest() {
        Integer userId = 1;
        Integer offset = 0;
        Integer limit = 10;
        mockMvc.perform(get("/items").header("X-Sharer-User-Id", userId).param("from", "0").param("size", "10"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(itemService).personalItems(userId, offset, limit);
    }

    @SneakyThrows
    @Test
    void searchItem() {
        Integer offset = 0;
        Integer limit = 10;
        String text = "test";
        mockMvc.perform(get("/items/search").param("text", "test").param("from", "0").param("size", "10"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(itemService).search(text, offset, limit);
    }

    @SneakyThrows
    @Test
    void addComment() {
        Integer itemId = 1;
        Integer userId = 1;
        CommentDto commentDto = CommentDto.builder()
            .text("test comment")
            .created(LocalDateTime.now())
            .build();
        when(itemService.addComment(any(), any(), any())).thenReturn(commentDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                .header("X-Sharer-User-Id", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(commentDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }

    @SneakyThrows
    @Test
    void addCommentCommentIsNotValidBadRequest() {
        Integer itemId = 1;
        Integer userId = 1;
        CommentDto commentDto = CommentDto.builder()
            .text("")
            .created(LocalDateTime.now())
            .build();
        when(itemService.addComment(itemId, userId, commentDto)).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                .header("X-Sharer-User-Id", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(commentDto)))
            .andExpect(status().isBadRequest())
            .andDo(print());

        verify(itemService, never()).addComment(itemId, userId, commentDto);
    }
}