package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ItemMapperTest {

    @Autowired
    private ItemMapperImpl itemMapper;

    LocalDateTime dateTime = LocalDateTime.of(2023, 12, 12, 12, 12);

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
        .description("test request")
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
    ItemDto testItemDto = ItemDto.builder()
        .id(1)
        .name("item")
        .description("testItem")
        .available(true)
        .requestId(1)
        .build();

    @Test
    void toItemDtoItemNotNullTest() {
        ItemDto actualItemDto = itemMapper.toItemDto(testItem);
        assertEquals(testItemDto.getId(), actualItemDto.getId());
        assertEquals(testItemDto.getName(), actualItemDto.getName());
        assertEquals(testItemDto.getDescription(), actualItemDto.getDescription());
        assertEquals(testItemDto.getAvailable(), actualItemDto.getAvailable());
        assertEquals(testItemDto.getRequestId(), actualItemDto.getRequestId());
    }

    @Test
    void toItemDtoItemNullTest() {
        ItemDto actualItemDto = itemMapper.toItemDto(null);
        assertNull(actualItemDto);
    }

    @Test
    void toItemItemDtoNotNullTest() {
        Item actualItem = itemMapper.toItem(testItemDto);
        assertEquals(testItemDto.getId(), actualItem.getId());
        assertEquals(testItemDto.getName(), actualItem.getName());
        assertEquals(testItemDto.getDescription(), actualItem.getDescription());
        assertEquals(testItemDto.getAvailable(), actualItem.getAvailable());
    }

    @Test
    void toItemItemDtoNullTest() {
        Item actualItem = itemMapper.toItem(null);
        assertNull(actualItem);
    }
}
