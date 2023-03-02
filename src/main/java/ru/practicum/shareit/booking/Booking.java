package ru.practicum.shareit.booking;

import lombok.Data;

import java.util.Date;

@Data
public class Booking {
    private Date dateStart;
    private Date dateEnd;
    private boolean status;
    private Integer itemId;
    private Integer userBookedId;
    private Integer id;
}
