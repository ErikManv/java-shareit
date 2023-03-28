package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    User testOwner = User.builder()
        .email("email@email.ru")
        .name("testOwner")
        .id(1)
        .build();
    UserDto testBookerDto = UserDto.builder()
        .email("email@email.ru")
        .name("testUser")
        .id(2)
        .build();
    User testBooker = User.builder()
        .email("email@email.ru")
        .name("testUser")
        .id(2)
        .build();
    ItemDto testItemDto = ItemDto.builder()
        .id(1)
        .name("testItem")
        .description("testItem owner")
        .available(true)
        .build();
    Item testItem = Item.builder()
        .id(1)
        .name("testItem")
        .description("testItem owner")
        .owner(testOwner)
        .available(true)
        .build();
    Booking expectedBooking = Booking.builder()
        .start(LocalDateTime.now())
        .end(LocalDateTime.now())
        .item(testItem)
        .booker(testBooker)
        .status(Status.WAITING)
        .build();
    BookingDto expectedBookingDto = BookingDto.builder()
        .start(LocalDateTime.now())
        .end(LocalDateTime.now())
        .item(testItemDto)
        .booker(testBookerDto)
        .build();

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingDtoMapper bookingDtoMapper;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Captor
    private ArgumentCaptor<Booking> argumentCaptor;
    @InjectMocks
    BookingServiceImpl bookingService;

    @Test
    void getBookingByIdCorrectTest() {
        Integer bookingId = 1;
        Integer userId = 2;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));
        when(bookingDtoMapper.toBookingDto(any())).thenReturn(expectedBookingDto);

        BookingDto actualBooking = bookingService.getBookingById(bookingId, userId);

        assertEquals(expectedBookingDto, actualBooking);
    }

    @Test
    void getBookingByIdBookingNotFoundThrowBookingNotFoundException() {
        Integer bookingId = 1;
        Integer userId = 2;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(bookingId, userId));
    }

    @Test
    void getBookingByIdUserIncorrectThrowBookingNotFoundException() {
        Integer bookingId = 1;
        Integer userId = 0;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(bookingId, userId));
    }

    @Test
    void getAllBookingsOfUserStateALLTest() {
        String state = "ALL";
        Integer userId = 1;
        Integer offset = 0;
        Integer limit = 10;

        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(any(), any()))
            .thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));

        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUserStateCURRENTTest() {
        String state = "CURRENT";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(any(), any()))
            .thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));

        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUserStatePASTTest() {
        String state = "PAST";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(any(), any()))
            .thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));

        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUserStateFUTURETest() {
        String state = "FUTURE";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(any(), any()))
            .thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));

        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUserStateWAITINGTest() {
        String state = "WAITING";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(any(), any()))
            .thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));

        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUserStateREJECTEDTest() {
        String state = "REJECTED";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(any(), any()))
            .thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));

        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUserUnknownStateThrowStatusDoesntExistException() {
        String state = "TEST";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;

        List<Item> ownerItems = new ArrayList<>();

        ownerItems.add(testItem);

        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));

        assertThrows(StatusDoesntExistException.class,
            () -> bookingService.getAllBookingsOfUser(userId, state, offset, limit));
    }

    @Test
    void getAllItemsBookingsOfOwnerStateALLTest() {
        String state = "ALL";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));
        when(itemRepository.findByOwner(testOwner)).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllItemsBookingsOfOwnerStateCURRENTTest() {
        String state = "CURRENT";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));
        when(itemRepository.findByOwner(testOwner)).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllItemsBookingsOfOwnerStatePASTTest() {
        String state = "PAST";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));
        when(itemRepository.findByOwner(testOwner)).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllItemsBookingsOfOwnerStateFUTURETest() {
        String state = "FUTURE";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));
        when(itemRepository.findByOwner(testOwner)).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllItemsBookingsOfOwnerStateWAITINGTest() {
        String state = "WAITING";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));
        when(itemRepository.findByOwner(testOwner)).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllItemsBookingsOfOwnerStateREJECTEDTest() {
        String state = "REJECTED";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));
        when(itemRepository.findByOwner(testOwner)).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllItemsBookingsOfOwnerUnknownStateThrowStatusDoesntExistException() {
        String state = "TEST";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testOwner));
        when(itemRepository.findByOwner(testOwner)).thenReturn(ownerItems);

        assertThrows(StatusDoesntExistException.class, () -> bookingService.getAllItemsBookingsOfOwner(userId, state,
            offset, limit));
    }

    @Test
    void addBookingEndBeforeStart_thenEndBeforeStartExceptionThrown() {
        Integer userId = 0;
        BookingDtoInput bookingDtoIn = BookingDtoInput.builder()
            .start(LocalDateTime.now().plusDays(5))
            .end(LocalDateTime.now())
            .itemId(1)
            .build();
        assertThrows(TimelineException.class, () -> bookingService.addBooking(bookingDtoIn, userId));
    }

    @Test
    void addBookingUserCantBookOwnItemException() {
        Integer userId = 0;

        BookingDtoInput bookingDtoIn = BookingDtoInput.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(5))
            .itemId(1)
            .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(testOwner));
        when(itemRepository.findById(any())).thenReturn(Optional.of(testItem));

        assertThrows(BookingErrorException.class, () -> bookingService.addBooking(bookingDtoIn, userId));
    }

    @Test
    void addBookingItemUnavailableThrowItemUnavailableException() {
        Integer userId = 0;
        BookingDtoInput bookingDtoIn = BookingDtoInput.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(5))
            .itemId(1)
            .build();

        Item testItem2 = Item.builder()
            .id(1)
            .name("testItem")
            .description("testItem owner")
            .owner(testBooker)
            .available(false)
            .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(testOwner));
        when(itemRepository.findById(any())).thenReturn(Optional.of(testItem2));

        assertThrows(ItemUnvailableException.class, () -> bookingService.addBooking(bookingDtoIn, userId));
    }

    @Test
    void addBookingCorrectTest() {
        Integer userId = 0;
        BookingDtoInput bookingDtoIn = BookingDtoInput.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(5))
            .itemId(1)
            .build();

        Item testItem2 = Item.builder()
            .id(1)
            .name("testItem")
            .description("testItem owner")
            .owner(testBooker)
            .available(true)
            .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(testOwner));
        when(itemRepository.findById(any())).thenReturn(Optional.of(testItem2));
        when(bookingRepository.save(any())).thenReturn(expectedBooking);
        when(bookingDtoMapper.toBookingDto(any())).thenReturn(expectedBookingDto);

        assertEquals(expectedBookingDto, bookingService.addBooking(bookingDtoIn, userId));
    }

    @Test
    void approveBookingThrowDoubleApprovingException() {
        Integer userId = 1;
        Booking booking = Booking.builder()
            .id(0)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .item(testItem)
            .booker(testBooker)
            .status(Status.APPROVED)
            .build();
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));

        assertThrows(DoubleApprovalException.class, () -> bookingService.approveBooking(booking.getId(), userId, true));
    }

    @Test
    void approveBookingCorrectTest() {
        Integer userId = 1;
        Booking booking = Booking.builder()
            .id(0)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .item(testItem)
            .booker(testBooker)
            .status(Status.WAITING)
            .build();

        Booking expectedBooking = Booking.builder()
            .id(0)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .item(testItem)
            .booker(testBooker)
            .status(Status.APPROVED)
            .build();
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));

        bookingService.approveBooking(booking.getId(), userId, true);

        verify(bookingDtoMapper).toBookingDto(argumentCaptor.capture());
        Booking updatedBooking = argumentCaptor.getValue();

        assertEquals(expectedBooking.getStatus(), updatedBooking.getStatus());
    }

    @Test
    void approveBookingApprovedIsFalseRejectedReturn() {
        Integer userId = 1;
        Booking booking = Booking.builder()
            .id(0)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .item(testItem)
            .booker(testBooker)
            .status(Status.WAITING)
            .build();

        Booking expectedBooking = Booking.builder()
            .id(0)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .item(testItem)
            .booker(testBooker)
            .status(Status.REJECTED)
            .build();
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));

        bookingService.approveBooking(booking.getId(), userId, false);

        verify(bookingDtoMapper).toBookingDto(argumentCaptor.capture());
        Booking updatedBooking = argumentCaptor.getValue();

        assertEquals(expectedBooking.getStatus(), updatedBooking.getStatus());
    }

    @Test
    void approveBookingUserIsNotOwnerThrowBookingErrorException() {
        Integer userId = 2;
        Booking booking = Booking.builder()
            .id(0)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .item(testItem)
            .booker(testBooker)
            .status(Status.WAITING)
            .build();
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));

        assertThrows(BookingErrorException.class, () -> bookingService.approveBooking(booking.getId(), userId, true));
    }
}