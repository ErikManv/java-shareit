package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.dto.ItemShortMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ItemShortDtoMapperTest {
    @Autowired
    ItemShortMapperImpl itemShortMapper;

    LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 12, 1);

    User testOwner = User.builder()
        .id(1)
        .name("test")
        .email("test@email.ru")
        .build();

    User testRequester = User.builder()
        .id(2)
        .name("test1")
        .email("test1@email.ru")
        .build();

    ItemRequest testItemRequest = ItemRequest.builder()
        .id(1)
        .description("testRequest")
        .owner(testRequester)
        .created(dateTime)
        .build();

    Item testItem = Item.builder()
        .id(1)
        .name("item")
        .owner(testOwner)
        .description("testItem")
        .available(true)
        .request(testItemRequest)
        .build();
    ItemShortDto testItemShortDto = ItemShortDto.builder()
        .id(1)
        .name("item")
        .ownerId(testOwner.getId())
        .description("testItem")
        .available(true)
        .requestId(testItemRequest.getId())
        .build();

    @Test
    void toDTO_whenItemNotNull_thenReturnItemShortDto() {
        ItemShortDto actualItemShortDto = itemShortMapper.toDto(testItem);
        assertEquals(testItemShortDto.getId(), actualItemShortDto.getId());
        assertEquals(testItemShortDto.getName(), actualItemShortDto.getName());
        assertEquals(testItemShortDto.getOwnerId(), actualItemShortDto.getOwnerId());
        assertEquals(testItemShortDto.getDescription(), actualItemShortDto.getDescription());
        assertEquals(testItemShortDto.getAvailable(), actualItemShortDto.getAvailable());
        assertEquals(testItemShortDto.getRequestId(), actualItemShortDto.getRequestId());
    }

    @Test
    void toDTO_whenItemNull_thenReturnNull() {
        ItemShortDto actualItemShortDto = itemShortMapper.toDto(null);
        assertNull(actualItemShortDto);
    }
}
