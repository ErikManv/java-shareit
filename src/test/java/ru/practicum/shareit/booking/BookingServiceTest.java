package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        .name("test Owner")
        .id(1)
        .build();
    UserDto testBookerDto = UserDto.builder()
        .email("email@email.ru")
        .name("test User")
        .id(2)
        .build();
    User testBooker = User.builder()
        .email("email@email.ru")
        .name("test User")
        .id(2)
        .build();
    ItemDto testItemDto = ItemDto.builder()
        .id(1)
        .name("test item")
        .description("Test item of test Owner")
        .available(true)
        .build();
    Item testItem = Item.builder()
        .id(1)
        .name("test item")
        .description("Test item of test Owner")
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
    private BookingDtoMapper bookingMapper;
    @Mock
    private UserRepository userService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Captor
    private ArgumentCaptor<Booking> argumentCaptor;
    @InjectMocks
    BookingServiceImpl bookingService;

    @Test
    void getBookingById_whenBookingFound_thenReturnBooking() {
        Integer bookingId = 1;
        Integer userId = 2;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));
        when(bookingMapper.toBookingDto(any())).thenReturn(expectedBookingDto);

        BookingDto actualBooking = bookingService.getBookingById(bookingId, userId);

        assertEquals(expectedBookingDto, actualBooking);
    }

    @Test
    void getBookingById_whenBookingNotFound_thenBookingNotFoundExceptionThrown() {
        Integer bookingId = 1;
        Integer userId = 2;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(bookingId, userId));
    }

    @Test
    void getBookingById_whenUserIncorrect_thenBookingNotFoundExceptionThrown() {
        Integer bookingId = 1;
        Integer userId = 0;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(bookingId, userId));
    }

    @Test
    void getAllBookingsOfUser_whenStateALL_thenReturnList() {
        String state = "ALL";
        Integer userId = 0;
        Integer offset = 0;
        Integer page = 0;
        Integer limit = 10;
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(userId, PageRequest.of(page, limit, Sort.by("id")
            .descending()))).thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(new User());

        userRepository.save(testOwner);
        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUser_whenStateCURRENT_thenReturnList() {
        String state = "CURRENT";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(any(), any()))
            .thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(new User());

        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUser_whenStatePAST_thenReturnList() {
        String state = "PAST";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(any(), any()))
            .thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(new User());

        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUser_whenStateFUTURE_thenReturnList() {
        String state = "FUTURE";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(any(), any()))
            .thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(new User());

        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUser_whenStateWAITING_thenReturnList() {
        String state = "WAITING";
        Integer userId = 0;
        Integer offset = 0;
        Integer page = 0;
        Integer limit = 10;
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(any(), any()))
            .thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(new User());

        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUser_whenStateREJECTED_thenReturnList() {
        String state = "REJECTED";
        Integer userId = 0;
        Integer offset = 0;
        Integer page = 0;
        Integer limit = 10;
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(any(), any()))
            .thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(new User());

        List<BookingDto> actual = bookingService.getAllBookingsOfUser(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllBookingsOfUser_whenUnknownState_thenUnsupportedStatusExceptionThrown() {
        String state = "TEST";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        when(userService.getById(userId)).thenReturn(testOwner);

        assertThrows(StatusDoesntExistException.class,
            () -> bookingService.getAllBookingsOfUser(userId, state, offset, limit));
    }

    @Test
    void getAllItemsBookingsOfOwner_whenStateALL_thenReturnList() {
        String state = "ALL";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(testOwner);
        when(itemRepository.findByOwner(userService.getById(userId))).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllItemsBookingsOfOwner_whenStateCURRENT_thenReturnList() {
        String state = "CURRENT";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(testOwner);
        when(itemRepository.findByOwner(userService.getById(userId))).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllItemsBookingsOfOwner_whenStatePAST_thenReturnList() {
        String state = "PAST";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(testOwner);
        when(itemRepository.findByOwner(userService.getById(userId))).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllItemsBookingsOfOwner_whenStateFUTURE_thenReturnList() {
        String state = "FUTURE";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(testOwner);
        when(itemRepository.findByOwner(userService.getById(userId))).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllItemsBookingsOfOwner_whenStateWAITING_thenReturnList() {
        String state = "WAITING";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(testOwner);
        when(itemRepository.findByOwner(userService.getById(userId))).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllItemsBookingsOfOwner_whenStateREJECTED_thenReturnList() {
        String state = "REJECTED";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(any(), any(), any())).thenReturn(Page.empty());
        when(userService.getById(userId)).thenReturn(testOwner);
        when(itemRepository.findByOwner(userService.getById(userId))).thenReturn(ownerItems);

        List<BookingDto> actual = bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit);
        assertEquals(0, actual.size());
    }

//    @Test
//    void getAllItemsBookingsOfOwner_whenNoItems_thenUserHaveNotAnyItemExceptionThrown() {
//        String state = "ALL";
//        Integer userId = 0;
//        Integer offset = 0;
//        Integer limit = 10;
//        List<Item> ownerItems = new ArrayList<>();
//        when(userService.getById(userId)).thenReturn(testOwner);
//        when(itemRepository.findByOwner(userService.getById(userId))).thenReturn(ownerItems);
//
//        assertThrows(UserHaveNotAnyItemException.class, () -> bookingService.getAllItemsBookingsOfOwner(userId, state,
//            offset, limit));
//    }

    @Test
    void getAllItemsBookingsOfOwner_whenUnknownState_thenUnsupportedStatusExceptionThrown() {
        String state = "TEST";
        Integer userId = 0;
        Integer offset = 0;
        Integer limit = 10;
        List<Item> ownerItems = new ArrayList<>();
        ownerItems.add(testItem);
        when(userService.getById(userId)).thenReturn(testOwner);
        when(itemRepository.findByOwner(userService.getById(userId))).thenReturn(ownerItems);

        assertThrows(StatusDoesntExistException.class, () -> bookingService.getAllItemsBookingsOfOwner(userId, state,
            offset, limit));
    }

    @Test
    void addBooking_whenEndBeforeStart_thenEndBeforeStartExceptionThrown() {
        Integer userId = 0;
        BookingDtoInput bookingDtoIn = BookingDtoInput.builder()
            .start(LocalDateTime.now().plusDays(5))
            .end(LocalDateTime.now())
            .itemId(1)
            .build();
        assertThrows(TimelineException.class, () -> bookingService.addBooking(bookingDtoIn, userId));
    }

//    @Test
//    void addBooking_whenUserIsOwner_thenBookingUnavailableExceptionThrown() {
//        Integer userId = 0;
//        BookingDtoInput bookingDtoIn = BookingDtoInput.builder()
//            .start(LocalDateTime.now())
//            .end(LocalDateTime.now().plusDays(5))
//            .itemId(1)
//            .build();
//
//        when(userService.getById(userId)).thenReturn(testOwner);
//        when(itemService.getItem(bookingDtoIn.getItemId())).thenReturn(testItem);
//
//        assertThrows(BookingUnavailableException.class, () -> bookingService.addBooking(userId, bookingDtoIn));
//    }
//
//    @Test
//    void addBooking_whenItemUnavailable_thenItemUnavailableExceptionThrown() {
//        Integer userId = 0;
//        BookingDtoInput bookingDtoIn = BookingDtoInput.builder()
//            .start(LocalDateTime.now())
//            .end(LocalDateTime.now().plusDays(5))
//            .itemId(1)
//            .build();
//        testItem.setAvailable(false);
//
//        when(userService.getById(userId)).thenReturn(testBooker);
//        when(itemService.getItem(bookingDtoIn.getItemId())).thenReturn(testItem);
//
//        assertThrows(ItemUnavailableException.class, () -> bookingService.addBooking(userId, bookingDtoIn));
//        testItem.setAvailable(true);
//    }
//
//    @Test
//    void addBooking_whenDataCorrect_thenSave() {
//        Integer userId = 0;
//        BookingDtoInput bookingDtoIn = BookingDtoInput.builder()
//            .start(LocalDateTime.now())
//            .end(LocalDateTime.now().plusDays(5))
//            .itemId(1)
//            .build();
//
//        when(userService.getById(userId)).thenReturn(testBooker);
//        when(itemService.getItem(bookingDtoIn.getItemId())).thenReturn(testItem);
//        when(bookingRepository.save(any())).thenReturn(expectedBooking);
//        when(bookingMapper.toBookingDto(any())).thenReturn(expectedBookingDto);
//
//        assertEquals(expectedBookingDto, bookingService.addBooking(userId, bookingDtoIn));
//    }

    @Test
    void approveBooking_whenBookingApprovedDouble_thenDoubleApprovingExceptionThrown() {
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
    void approveBooking_whenDataCorrectAndApprovedIsTrue_thenStatusApprovedReturn() {
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

        verify(bookingMapper).toBookingDto(argumentCaptor.capture());
        Booking updatedBooking = argumentCaptor.getValue();

        assertEquals(expectedBooking.getStatus(), updatedBooking.getStatus());
    }

    @Test
    void approveBooking_whenDataCorrectAndApprovedIsFalse_thenStatusRejectedReturn() {
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

        verify(bookingMapper).toBookingDto(argumentCaptor.capture());
        Booking updatedBooking = argumentCaptor.getValue();

        assertEquals(expectedBooking.getStatus(), updatedBooking.getStatus());
    }

    @Test
    void approveBooking_whenUserIsNotOwner_thenBookingUnavailableExceptionThrown() {
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