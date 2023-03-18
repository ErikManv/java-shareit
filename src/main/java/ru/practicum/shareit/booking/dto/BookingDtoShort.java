package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
public class BookingDtoShort {

    public BookingDtoShort(Integer id, LocalDateTime start, LocalDateTime end) {
        this.end = end;
        this.start = start;
        this.itemId = id;
    }

    @NotNull
    private Integer itemId;

    @NotNull
    @Future
    private final LocalDateTime start;

    @NotNull
    @Future
    private final LocalDateTime end;
}
