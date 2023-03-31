package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
												 @Valid @RequestBody BookingDtoInput bookingDtoInput) {
		log.info("Creating booking {}, userId={}", bookingDtoInput, userId);
		System.out.println(bookingDtoInput);
		return bookingClient.bookItem(userId, bookingDtoInput);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(@PathVariable Integer bookingId,
													 @RequestHeader("X-Sharer-User-Id") Integer userId,
													 @RequestParam Boolean approved) {
		log.info("Approving booking with id-{} ", userId);
		return bookingClient.approveBooking(bookingId, userId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@PathVariable Integer bookingId,
													 @RequestHeader("X-Sharer-User-Id") Integer userId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping()
	public ResponseEntity<Object> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Integer userId,
																 @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
																 @RequestParam(value = "from", defaultValue = "0", required = false)
																 @Min(0) Integer from,
																 @RequestParam(value = "size", defaultValue = "10", required = false)
																 @Min(1) @Max(50) Integer size) {
		BookingState state = BookingState.from(stateParam)
			.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking of user with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookingsOfUser(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllItemsBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
																	   @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
																	   @RequestParam(value = "from", defaultValue = "0",
																		   required = false) @Min(0) Integer from,
																	   @RequestParam(value = "size", defaultValue = "10",
																		   required = false) @Min(1) @Max(50) Integer size) {
		BookingState state = BookingState.from(stateParam)
			.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking of user with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookingsOfOwner(userId, state, from, size);
	}
}
