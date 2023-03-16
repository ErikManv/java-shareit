package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.service.BookingService;

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
    public ResponseEntity<BookingDto> addBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @Validated @RequestBody BookingDtoShort bookingDtoShort) {
        return new ResponseEntity<>(bookingService.addBooking(bookingDtoShort, userId), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approveBooking(@PathVariable Integer bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @NotEmpty @RequestParam Boolean approved) {
        return new ResponseEntity<>(bookingService.approveBooking(bookingId, userId, approved), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Integer bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return new ResponseEntity<>(bookingService.getBookingById(bookingId, userId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<BookingDto>> getAllBookingsOfUser(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestParam(defaultValue = "ALL", required = false) String state) {
        return new ResponseEntity<>(bookingService.getAllBookingsOfUser(userId, state), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getAllItemsBookingsOfOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                       @RequestParam(defaultValue = "ALL", required = false) String state) {
        return new ResponseEntity<>(bookingService.getAllItemsBookingsOfOwner(userId, state), HttpStatus.OK);
    }
}
