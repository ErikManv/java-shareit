package ru.practicum.shareit.booking;

import lombok.Data;

import java.util.Date;

@Data
public class Booking {
    Date dateStart;
    Date dateEnd;
    boolean status;
    Integer ItemId;
    Integer userBookedId;
    Integer id;
}
