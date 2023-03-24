package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ItemServiceTest.class)
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    User testOwner = User.builder()
        .email("email@email.ru")
        .name("test Owner")
        .id(1)
        .build();

    User testUser = User.builder()
        .email("email@email.ru")
        .name("test User")
        .id(2)
        .build();
    ItemDto testItemDto = ItemDto.builder()
        .id(1)
        .name("test item")
        .description("Test item of test Owner")
        .available(true)
        .requestId(1)
        .build();
    Item testItem = Item.builder()
        .id(1)
        .name("test item")
        .description("Test item of test Owner")
        .owner(testUser)
        .available(true)
        .build();

    ItemRequest itemRequest = ItemRequest.builder()
        .id(1)
        .owner(testUser)
        .description("Description")
        .created(LocalDateTime.now())
        .build();


    @Mock
    private UserRepository userService;
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemMapper itemMapper;

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    public void addItemTest() {
        Integer userId = 1;
        Item testItem = Item.builder()
            .id(1)
            .name("test item")
            .description("Test item of test Owner")
            .available(true)
            .build();
        when(itemMapper.toItem(testItemDto)).thenReturn(testItem);
        when(userService.getById(userId)).thenReturn(testOwner);
        when(itemRequestRepository.findById(any())).thenReturn(Optional.of(itemRequest));

        itemService.addItem(testItemDto, userId);

        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals(testItem.getName(), savedItem.getName());
        assertEquals(testItem.getDescription(), savedItem.getDescription());
        assertEquals(testItem.getOwner().getId(), savedItem.getOwner().getId());
        assertEquals(testItem.getAvailable(), savedItem.getAvailable());
        assertEquals(itemRequest.getId(), savedItem.getRequest().getId());
    }
}