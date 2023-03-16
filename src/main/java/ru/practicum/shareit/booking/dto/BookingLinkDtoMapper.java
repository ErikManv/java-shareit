package ru.practicum.shareit.booking.dto;

import org.mapstruct.*;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingLinkDtoMapper {

    @Mapping(target = "bookerId", source = "booker.id")
    BookingLinkDto toDto(Booking booking);
}