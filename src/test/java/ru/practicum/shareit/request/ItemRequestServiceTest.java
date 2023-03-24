package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemShortMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    User testUser;
    User testOwner;
    Item testItem;
    List<Item> items;

    @BeforeEach
        void init() {
        testOwner = User.builder()
            .email("email@email.ru")
            .name("testOwner")
            .id(1)
            .build();
        testUser = User.builder()
            .email("email@email.ru")
            .name("testUser")
            .id(2)
            .build();
        testItem = Item.builder()
            .id(1)
            .name("testItem")
            .description("testItem owner")
            .owner(testOwner)
            .available(true)
            .build();
        items = new ArrayList<>();
    }

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRequestMapper itemRequestMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemShortMapper itemShortMapper;
    @Captor
    private ArgumentCaptor<ItemRequest> itemRequestArgumentCaptor;
    @InjectMocks
    ItemRequestServiceImpl itemRequestService;


    @Test
    void addItemRequest() {
        Integer userId = 1;
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("description")
            .build();
        ItemRequest itemRequest = ItemRequest.builder()
            .description("description")
            .owner(testOwner)
            .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(testOwner));

        itemRequestService.addItemRequest(itemRequestDto, userId);

        verify(itemRequestRepository).save(itemRequestArgumentCaptor.capture());
        ItemRequest savedItemRequest = itemRequestArgumentCaptor.getValue();

        assertEquals(itemRequest.getDescription(), savedItemRequest.getDescription());
        assertEquals(itemRequest.getOwner().getId(), savedItemRequest.getOwner().getId());
    }

    @Test
    void getAllOwnRequestsCorrect() {
        Integer userId = 0;

        when(userRepository.findById(any())).thenReturn(Optional.of(testOwner));
        when(itemRequestRepository.findByOwner(any())).thenReturn(new ArrayList<>());

        assertEquals(0, itemRequestService.getOwnRequests(userId).size());
    }

    @Test
    void getAllOthersRequest() {
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;

        when(itemRequestRepository.findAllByOwnerIdNot(any(), any())).thenReturn(Page.empty());

        assertEquals(0, itemRequestService.getAllOthersRequests(userId, offset, limit).size());
    }

    @Test
    void getRequestByIdItemRequestNotNull() {
        Integer userId = 1;
        Integer itemRequestId = 1;
        ItemRequest itemRequest = ItemRequest.builder()
            .description("description")
            .build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("description")
            .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(testOwner));
        when(itemRequestRepository.findById(any())).thenReturn(Optional.of(itemRequest));
        when(itemRequestMapper.toDto(any())).thenReturn(itemRequestDto);
        assertEquals(itemRequestDto.getDescription(), itemRequestService.getRequestById(userId, itemRequestId).getDescription());
    }

    @Test
    void getItemRequestById_whenItemRequestNotExists_thenItemNotFoundExceptionThrown() {
        Integer userId = 1;
        Integer itemRequestId = 1;
        when(userRepository.findById(any())).thenReturn(Optional.of(testOwner));
        when(itemRequestRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ItemRequestNotFoundException.class, () -> itemRequestService.getRequestById(userId, itemRequestId));
    }
}
