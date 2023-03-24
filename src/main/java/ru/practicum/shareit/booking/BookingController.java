package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
                                                @Valid @RequestBody BookingDtoInput bookingDtoInput) {
        return new ResponseEntity<>(bookingService.addBooking(bookingDtoInput, userId), HttpStatus.OK);
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
    public ResponseEntity<List<BookingDto>> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestParam(defaultValue = "ALL", required = false) String state,
                                                                 @RequestParam(value = "from", defaultValue = "0", required = false)
                                                                     @Min(0) Integer offset,
                                                                 @RequestParam(value = "size", defaultValue = "10", required = false)
                                                                     @Min(1) @Max(50) Integer limit) {
        return new ResponseEntity<>(bookingService.getAllBookingsOfUser(userId, state, offset, limit), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getAllItemsBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                       @RequestParam(defaultValue = "ALL", required = false) String state,
                                                                       @RequestParam(value = "from", defaultValue = "0",
                                                                           required = false) @Min(0) Integer offset,
                                                                       @RequestParam(value = "size", defaultValue = "10",
                                                                           required = false) @Min(1) @Max(50) Integer limit) {
        return new ResponseEntity<>(bookingService.getAllItemsBookingsOfOwner(userId, state, offset, limit), HttpStatus.OK);
    }
}
