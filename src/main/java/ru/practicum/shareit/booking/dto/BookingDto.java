package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    private final Integer id;

    private final LocalDateTime start;

    private final LocalDateTime end;

    private final UserDto booker;

    private final ItemDto item;

    private final Status status;
}