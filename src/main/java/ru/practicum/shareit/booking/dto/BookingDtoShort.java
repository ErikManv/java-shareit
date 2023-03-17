package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingDtoShort {
    @NotNull
    private Integer itemId;

    @NotNull
    @FutureOrPresent
    private final LocalDateTime start;

    @NotNull
    @Future
    private final LocalDateTime end;
}
