package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingLinkDto;
import ru.practicum.shareit.booking.dto.BookingLinkDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.CommentWithoutBookingException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    User ownerTest = User.builder()
        .email("email@email.ru")
        .name("testOwner")
        .id(1)
        .build();

    User userTest = User.builder()
        .email("email@email.ru")
        .name("testUser")
        .id(2)
        .build();
    ItemDto itemDtoTest = ItemDto.builder()
        .id(1)
        .name("test")
        .description("testDesc")
        .available(true)
        .requestId(1)
        .build();

    Item testItem = Item.builder()
        .id(1)
        .name("test")
        .owner(ownerTest)
        .description("testDesc")
        .available(true)
        .build();

    ItemRequest itemRequest = ItemRequest.builder()
        .id(1)
        .owner(userTest)
        .description("description")
        .created(LocalDateTime.now())
        .build();

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private BookingLinkDtoMapper bookingLinkDtoMapper;
    @Mock
    private CommentMapper commentMapper;
    @Captor
    private ArgumentCaptor<Item> itemArgCaptor;
    @Captor
    private ArgumentCaptor<Comment> commentArgCaptor;
    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    public void addItemCorrectTest() {
        Integer userId = 1;
        Item testItem = Item.builder()
            .id(1)
            .name("test")
            .description("testDesc")
            .available(true)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(ownerTest));
        when(itemMapper.toItem(itemDtoTest)).thenReturn(testItem);
        when(itemRequestRepository.findById(any())).thenReturn(Optional.of(itemRequest));

        itemService.addItem(itemDtoTest, userId);

        verify(itemRepository).save(itemArgCaptor.capture());
        Item savedItem = itemArgCaptor.getValue();

        assertEquals(testItem.getName(), savedItem.getName());
        assertEquals(testItem.getDescription(), savedItem.getDescription());
        assertEquals(testItem.getOwner().getId(), savedItem.getOwner().getId());
        assertEquals(testItem.getAvailable(), savedItem.getAvailable());
        assertEquals(itemRequest.getId(), savedItem.getRequest().getId());
    }

    @Test
    void addItemUserNotFoundTest() {
        Integer userId = 0;

        assertThrows(UserNotFoundException.class, () -> itemService.addItem(itemDtoTest, userId));
    }

    @Test
    void updateItemCorrectTest() {
        Integer userId = 1;
        Integer itemId = 1;
        ItemDto updateItemDto = ItemDto.builder()
            .id(1)
            .name("test")
            .description("testDesc")
            .available(true)
            .build();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(testItem));

        itemService.updateItem(updateItemDto, itemId, userId);

        verify(itemRepository).save(itemArgCaptor.capture());
        Item updatedItem = itemArgCaptor.getValue();

        assertEquals(updateItemDto.getName(), updatedItem.getName());
        assertEquals(updateItemDto.getDescription(), updatedItem.getDescription());
        assertEquals(testItem.getOwner().getId(), updatedItem.getOwner().getId());
        assertEquals(updateItemDto.getAvailable(), updatedItem.getAvailable());
    }

    @Test
    public void updateItemItemNotFoundExceptionTest() {
        Integer userId = 0;
        Integer itemId = 2;
        assertThrows(ItemNotFoundException.class, () -> itemService.updateItem(itemDtoTest, itemId, userId));
    }

    @Test
    public void updateItemUserValidationExceptionTest() {
        Integer userId = 0;
        Integer itemId = 1;

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(testItem));

        assertThrows(UserValidationException.class, () -> itemService.updateItem(itemDtoTest, itemId, userId));
    }

    @Test
    public void getItemByIdCorrectReturnWithBookingTest() {
        Integer userId = 1;
        Integer itemId = 1;
        BookingLinkDto bookingShortDto = BookingLinkDto.builder()
            .id(1)
            .bookerId(1)
            .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(Booking.builder()
            .id(1)
            .status(Status.WAITING)
            .start(LocalDateTime.now().minusDays(2))
            .end(LocalDateTime.now().minusDays(1))
            .build());
        bookings.add(Booking.builder()
            .id(2)
            .status(Status.WAITING)
            .start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusDays(2))
            .build());

        when(itemRepository.findById(any())).thenReturn(Optional.of(testItem));

        when(bookingRepository.findAllByItem_Id(itemId)).thenReturn(bookings);
        when(bookingLinkDtoMapper.toDto(any())).thenReturn(bookingShortDto);
        when(itemMapper.toItemDto(any())).thenReturn(itemDtoTest);
        when(commentRepository.findAllByItem_Id(any())).thenReturn(new ArrayList<>());
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(testItem));


        ItemDto actualItemDto = itemService.getItemById(userId, itemId);

        assertNotNull(actualItemDto.getNextBooking());
        assertNotNull(actualItemDto.getLastBooking());
        assertNotNull(actualItemDto.getComments());
    }

    @Test
    void getItemByIdItemNotExistTest() {
        Integer itemId = 1;
        Integer userId = 1;
        when(itemRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(itemId, userId));
    }

    @Test
    void personalItemsTest() {
        Integer userId = 1;
        Integer offset = 0;
        Integer limit = 10;
        when(userRepository.findById(userId)).thenReturn(Optional.of(ownerTest));
        when(itemRepository.findByOwner(any(), any())).thenReturn(Page.empty());

        assertEquals(0, itemService.personalItems(userId, offset, limit).size());
    }

    @Test
    void searchWhenTextEmptyOrNullTest() {
        String text = "";
        Integer offset = 0;
        Integer limit = 10;

        when(itemRepository.findItemByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(any(), any(),
            any())).thenReturn(Page.empty());
        assertEquals(0, itemService.search(text, offset, limit).size());
    }

    @Test
    void searchWhenTextNotEmptyOrNullTest() {
        String text = "test";
        Integer offset = 0;
        Integer limit = 10;

        when(itemRepository.findItemByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(any(), any(),
            any())).thenReturn(Page.empty());
        assertEquals(0, itemService.search(text, offset, limit).size());
    }

    @Test
    void addCommentCorrect() {
        Integer userId = 2;
        Integer itemId = 1;
        CommentDto commentDto = CommentDto.builder()
            .text("commentText")
            .authorName(userTest.getName())
            .created(LocalDateTime.now())
            .build();

        Comment comment = Comment.builder()
            .text("commentText")
            .author(userTest)
            .item(testItem)
            .created(LocalDateTime.now())
            .build();

        Booking booking = Booking.builder()
            .start(LocalDateTime.now().minusDays(2))
            .end(LocalDateTime.now().minusDays(1))
            .item(testItem)
            .booker(userTest)
            .status(Status.APPROVED)
            .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(testItem));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userTest));
        when(bookingRepository.findAllByItem_IdAndBooker_Id(itemId, userId)).thenReturn(bookings);
        when(commentRepository.save(any())).thenReturn(comment);

        itemService.addComment(itemId, userId, commentDto);

        verify(commentMapper).toDto(commentArgCaptor.capture());
        Comment savedComment = commentArgCaptor.getValue();

        assertEquals(comment.getText(), savedComment.getText());
        assertEquals(comment.getItem().getId(), savedComment.getItem().getId());
        assertEquals(comment.getAuthor().getId(), savedComment.getAuthor().getId());
    }

    @Test
    void addCommentWhenBookingsIsEmptyThrowCommentWithoutBookingExceptionTest() {
        Integer itemId = 1;
        Integer bookerId = 1;

        CommentDto commentDto = CommentDto.builder()
            .text("commentText")
            .authorName(userTest.getName())
            .created(LocalDateTime.now())
            .build();

        when(bookingRepository.findAllByItem_IdAndBooker_Id(bookerId, itemId)).thenReturn(Collections.emptyList());

        assertThrows(CommentWithoutBookingException.class, () -> itemService.addComment(bookerId, itemId, commentDto));
    }
}