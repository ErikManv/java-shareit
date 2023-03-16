package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@NotEmpty @RequestHeader("X-Sharer-User-Id") Integer userId,
                              @Valid @RequestBody BookingDtoShort bookingDtoShort) {
        return bookingService.addBooking(bookingDtoShort, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable Integer bookingId,
                                     @NotEmpty @RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @NotEmpty @RequestParam Boolean approved) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Integer bookingId,
                                     @NotEmpty @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping()
    public List<BookingDto> getAllBookingsOfUser(@NotEmpty @RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.getAllBookingsOfUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookingsOfOwner(@NotEmpty @RequestHeader("X-Sharer-User-Id") Integer userId,
                                                       @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.getAllItemsBookingsOfOwner(userId, state);
    }
}
