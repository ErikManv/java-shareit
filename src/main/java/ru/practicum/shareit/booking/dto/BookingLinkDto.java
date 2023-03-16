package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BookingLinkDto {
    @NotNull
    private Integer id;

    @NotNull
    private Integer bookerId;
}
