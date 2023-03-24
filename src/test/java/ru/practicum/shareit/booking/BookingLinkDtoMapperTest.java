package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingLinkDto;
import ru.practicum.shareit.booking.dto.BookingLinkDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class BookingLinkDtoMapperTest {
    @Autowired
    BookingLinkDtoMapper bookingMapper;

    User testBooker = User.builder()
        .id(3)
        .name("Test2")
        .email("test2@email.ru")
        .build();

    Booking testBooking = Booking.builder()
        .id(1)
        .booker(testBooker)
        .build();

    BookingLinkDto testBookingShortDto = BookingLinkDto.builder()
        .id(1)
        .bookerId(testBooker.getId())
        .build();

    @Test
    void toDTO_whenBookingNotNull_thenReturnBookingShortDto() {
        BookingLinkDto actualBookingShortDto = bookingMapper.toDto(testBooking);
        assertEquals(testBookingShortDto.getId(), actualBookingShortDto.getId());
        assertEquals(testBookingShortDto.getBookerId(), actualBookingShortDto.getBookerId());
    }

    @Test
    void toDTO_whenBookingNull_thenReturnNull() {
        BookingLinkDto actualBookingShortDto = bookingMapper.toDto(null);
        assertNull(actualBookingShortDto);
    }
}
