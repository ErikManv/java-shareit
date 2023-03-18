package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDtoInput bookingDtoInput, Integer userId);

    BookingDto approveBooking(Integer bookingId, Integer userId, Boolean approved);

    BookingDto getBookingById(Integer bookingId, Integer userId);

    Booking getBooking(Integer bookingId);

    List<BookingDto> getAllBookingsOfUser(Integer userId, String state);

    List<BookingDto> getAllItemsBookingsOfOwner(Integer userId, String state);
}
