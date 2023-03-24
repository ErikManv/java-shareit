package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;

    @SneakyThrows
    @Test
    void getBookingById() {
        Integer userId = 1;
        Integer bookingId = 0;

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                .header("X-Sharer-User-Id", userId))
            .andDo(print())
            .andExpect(status().isOk());

        verify(bookingService).getBookingById(bookingId, userId);
    }

    @SneakyThrows
    @Test
    void getAllBookingsOfUser() {
        Integer userId = 1;
        String state = "ALL";
        Integer offset = 0;
        Integer limit = 10;

        mockMvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", userId)
                .param("state", "ALL")
                .param("from", "0")
                .param("size", "10"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(bookingService).getAllBookingsOfUser(userId, state, offset, limit);
    }

    @SneakyThrows
    @Test
    void getAllItemsBookingsOfOwner() {

        Integer userId = 1;
        String state = "ALL";
        Integer offset = 0;
        Integer limit = 10;

        mockMvc.perform(get("/bookings/owner")
                .header("X-Sharer-User-Id", userId)
                .param("state", "ALL")
                .param("from", "0")
                .param("size", "10"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(bookingService).getAllItemsBookingsOfOwner(userId, state, offset, limit);
    }

    @SneakyThrows
    @Test
    void addBookingBookingIsCorrect() {
        Integer userId = 1;
        BookingDtoInput bookingDtoInput = BookingDtoInput.builder()
            .itemId(1)
            .start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusDays(2))
            .build();

        mockMvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(bookingDtoInput)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(bookingService).addBooking(bookingDtoInput, userId);
    }

    @SneakyThrows
    @Test
    void addBooking_whenBookingIsNotCorrect_thenStatusIsBadRequest() {
        Integer userId = 1;
        BookingDtoInput bookingDtoInput = BookingDtoInput.builder()
            .itemId(1)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .build();

        mockMvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(bookingDtoInput)))
            .andExpect(status().isBadRequest())
            .andDo(print());

        verify(bookingService, never()).addBooking(bookingDtoInput, userId);
    }

    @SneakyThrows
    @Test
    void approveBooking() {

        Integer userId = 1;
        Integer bookingId = 0;
        Boolean approved = true;

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                .header("X-Sharer-User-Id", userId)
                .param("approved", "true"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(bookingService).approveBooking(bookingId, userId, approved);
    }
}
