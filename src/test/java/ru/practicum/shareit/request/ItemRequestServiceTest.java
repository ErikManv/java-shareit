package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemShortDto;
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
        void init()
    {
        testOwner = User.builder()
            .email("email@email.ru")
            .name("test Owner")
            .id(1)
            .build();
        testUser = User.builder()
            .email("email@email.ru")
            .name("test User")
            .id(2)
            .build();
        testItem = Item.builder()
            .id(1)
            .name("test item")
            .description("Test item of test Owner")
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
        Integer userId = 2;
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("Description")
            .build();
        ItemRequest itemRequest = ItemRequest.builder()
            .description("Description")
            .build();
        when(itemRequestMapper.toModel(any())).thenReturn(itemRequest);
        when(userRepository.getById(any())).thenReturn(testUser);

        itemRequestService.addItemRequest(itemRequestDto, userId);

        verify(itemRequestRepository).save(itemRequestArgumentCaptor.capture());
        ItemRequest savedItemRequest = itemRequestArgumentCaptor.getValue();

        assertEquals(itemRequest.getDescription(), savedItemRequest.getDescription());
        assertEquals(itemRequest.getOwner().getId(), savedItemRequest.getOwner().getId());
    }

    @Test
    void connectItems() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("Description")
            .build();
        ItemShortDto itemShortDto = ItemShortDto.builder()
            .id(1)
            .ownerId(1)
            .available(true)
            .description("Test item of test Owner")
            .requestId(1)
            .name("test item")
            .build();
        items.add(testItem);
        when(itemRepository.findByRequestId(any())).thenReturn(items);
        when(itemShortMapper.toDto(any())).thenReturn(itemShortDto);

        ItemRequestDto result = itemRequestService.getRequestById(itemRequestDto.getId(), 1);

        assertEquals(1, result.getItems().size());
    }

    @Test
    void getAllOwnItemRequests_whenDataCorrect_thenReturnList() {
        Integer userId = 0;
        when(userRepository.getById(any())).thenReturn(testUser);
        when(itemRequestRepository.findByOwner(any())).thenReturn(new ArrayList<>());

        assertEquals(0, itemRequestService.getOwnRequests(userId).size());
    }

    @Test
    void getAllItemRequestsOfUsers_whenDataCorrect_thenReturnList() {
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        when(itemRequestRepository.findAllByOwnerIdNot(any(), any())).thenReturn(Page.empty());

        assertEquals(0, itemRequestService.getAllOthersRequests(userId, offset, limit).size());
    }

    @Test
    void getItemRequestById_whenItemRequestNotNull_thenReturnList() {
        Integer userId = 1;
        Integer itemRequestId = 1;
        ItemRequest itemRequest = ItemRequest.builder()
            .description("Description")
            .build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("Description")
            .build();
        when(userRepository.getById(any())).thenReturn(testUser);
        when(itemRequestRepository.findById(any())).thenReturn(Optional.of(itemRequest));
        when(itemRequestMapper.toDto(any())).thenReturn(itemRequestDto);
        assertEquals(itemRequestDto.getDescription(), itemRequestService.getRequestById(userId, itemRequestId).getDescription());
    }

    @Test
    void getItemRequestById_whenItemRequestNotExists_thenItemNotFoundExceptionThrown() {
        Integer userId = 1;
        Integer itemRequestId = 1;
        when(userRepository.getById(any())).thenReturn(testUser);
        when(itemRequestRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(
            ChangeSetPersister.NotFoundException.class, () -> itemRequestService.getRequestById(userId, itemRequestId));
    }
}
