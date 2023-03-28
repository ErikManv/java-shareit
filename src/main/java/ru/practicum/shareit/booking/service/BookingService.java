package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;

import java.util.List;

@Service
public interface BookingService {
    BookingDto addBooking(BookingDtoInput bookingDtoInput, Integer userId);

    BookingDto approveBooking(Integer bookingId, Integer userId, Boolean approved);

    BookingDto getBookingById(Integer bookingId, Integer userId);

    List<BookingDto> getAllBookingsOfUser(Integer userId, String state, Integer offset, Integer limit);

    List<BookingDto> getAllItemsBookingsOfOwner(Integer userId, String state, Integer offset, Integer limit);
}
