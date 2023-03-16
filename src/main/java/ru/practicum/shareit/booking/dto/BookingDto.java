package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingDto {

    private final Integer id;

    @FutureOrPresent(message = "start не может быть раньше текущего времени ")
    private final LocalDateTime start;

    @Future(message = "end должен быть позже start")
    private final LocalDateTime end;

    @NotNull
    private final UserDto booker;

    @NotNull
    private final ItemDto item;

    @NotNull
    private final Status status;
}