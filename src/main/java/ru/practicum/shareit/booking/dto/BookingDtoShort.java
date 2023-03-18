package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class BookingDtoShort {
    @NotNull
    private Integer itemId;

    @NotNull
    @Future
    private final LocalDateTime start;

    @NotNull
    @Future
    private final LocalDateTime end;
}
