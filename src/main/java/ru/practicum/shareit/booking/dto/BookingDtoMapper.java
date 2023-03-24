package ru.practicum.shareit.booking.dto;

import org.mapstruct.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class, Status.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookingDtoMapper {

    BookingDto toBookingDto(Booking booking);

    Booking toBooking(BookingDto bookingDto);
}
