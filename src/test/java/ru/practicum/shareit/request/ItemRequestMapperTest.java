package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapperImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ItemRequestMapperTest {
    @Autowired
    private ItemRequestMapperImpl itemRequestMapper;

    LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 12, 1);

    User requester = User.builder()
        .id(1)
        .name("test")
        .email("test@email.ru")
        .build();

    ItemRequest testItemRequest = ItemRequest.builder()
        .id(1)
        .description("testRequest")
        .owner(requester)
        .created(dateTime)
        .build();

    ItemRequestDto testItemRequestDto = ItemRequestDto.builder()
        .id(1)
        .description("testRequest")
        .requesterId(1)
        .created(dateTime)
        .build();

    @Test
    void toDtoItemRequestNotNullTest() {
        ItemRequestDto actualItemRequestDto = itemRequestMapper.toDto(testItemRequest);
        System.out.println(actualItemRequestDto);
        assertEquals(testItemRequestDto.getId(), actualItemRequestDto.getId());
        assertEquals(testItemRequestDto.getDescription(), actualItemRequestDto.getDescription());
        assertEquals(testItemRequestDto.getRequesterId(), actualItemRequestDto.getRequesterId());
        assertEquals(testItemRequestDto.getCreated(), actualItemRequestDto.getCreated());
    }

    @Test
    void toDtoItemRequestNullTest() {
        ItemRequestDto actualItemRequestDto = itemRequestMapper.toDto(null);
        assertNull(actualItemRequestDto);
    }

    @Test
    void toItemRequestDtoNotNullTest() {
        ItemRequest testItemRequest = itemRequestMapper.toModel(testItemRequestDto);
        assertEquals(testItemRequestDto.getId(), testItemRequest.getId());
        assertEquals(testItemRequestDto.getDescription(), testItemRequest.getDescription());
        assertEquals(testItemRequestDto.getCreated(), testItemRequest.getCreated());
    }

    @Test
    void toItemRequestDtoNullTest() {
        ItemRequest testItemRequest = itemRequestMapper.toModel(null);
        assertNull(testItemRequest);
    }
}
