package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoMapperImpl;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class BookingMapperTest {

    @Autowired
    BookingDtoMapperImpl bookingDtoMapper;
    LocalDateTime start = LocalDateTime.of(2023, 11, 1, 12, 1);
    LocalDateTime end = LocalDateTime.of(2023, 12, 1, 12, 1);
    LocalDateTime dateTime = LocalDateTime.of(2024, 12, 1, 12, 1);

    User testOwner = User.builder()
        .id(1)
        .name("Test")
        .email("test@email.ru")
        .build();

    User testRequester = User.builder()
        .id(2)
        .name("Test1")
        .email("test1@email.ru")
        .build();

    User testBooker = User.builder()
        .id(3)
        .name("Test2")
        .email("test2@email.ru")
        .build();
    UserDto testBookerDto = UserDto.builder()
        .id(3)
        .name("Test2")
        .email("test2@email.ru")
        .build();

    ItemRequest testItemRequest = ItemRequest.builder()
        .id(1)
        .description("testRequest")
        .owner(testRequester)
        .created(dateTime)
        .build();

    Item testItem = Item.builder()
        .id(1)
        .name("Item")
        .owner(testOwner)
        .description("testItem")
        .available(true)
        .request(testItemRequest)
        .build();
    ItemDto testItemDto = ItemDto.builder()
        .id(1)
        .name("Item")
        .description("testItem")
        .available(true)
        .requestId(1)
        .build();

    Booking testBooking = Booking.builder()
        .id(1)
        .item(testItem)
        .booker(testBooker)
        .status(Status.WAITING)
        .start(start)
        .end(end)
        .build();

    BookingDto testBookingDto = BookingDto.builder()
        .id(1)
        .item(testItemDto)
        .booker(testBookerDto)
        .status(Status.WAITING)
        .start(start)
        .end(end)
        .build();

    @Test
    void toDtoBookingNotNullTest() {
        BookingDto actualBookingDto = bookingDtoMapper.toBookingDto(testBooking);
        assertEquals(testBookingDto.getId(), actualBookingDto.getId());
        assertEquals(testBookingDto.getItem(), actualBookingDto.getItem());
        assertEquals(testBookingDto.getBooker(), actualBookingDto.getBooker());
        assertEquals(testBookingDto.getStatus(), actualBookingDto.getStatus());
        assertEquals(testBookingDto.getStart(), actualBookingDto.getStart());
        assertEquals(testBookingDto.getEnd(), actualBookingDto.getEnd());
    }

    @Test
    void toDtoBookingNullTest() {
        BookingDto actualBookingDto = bookingDtoMapper.toBookingDto(null);
        assertNull(actualBookingDto);
    }

    @Test
    void toBookingBookingDtoNotNullTest() {
        Booking actualBooking = bookingDtoMapper.toBooking(testBookingDto);
        assertEquals(testBooking.getId(), actualBooking.getId());
        assertEquals(testBooking.getItem().getId(), actualBooking.getItem().getId());
        assertEquals(testBooking.getBooker().getId(), actualBooking.getBooker().getId());
        assertEquals(testBooking.getStatus(), actualBooking.getStatus());
        assertEquals(testBooking.getStart(), actualBooking.getStart());
        assertEquals(testBooking.getEnd(), actualBooking.getEnd());
    }

    @Test
    void toBookingBookingDtoNullTest() {
        Booking actualBooking = bookingDtoMapper.toBooking(null);
        assertNull(actualBooking);
    }
}