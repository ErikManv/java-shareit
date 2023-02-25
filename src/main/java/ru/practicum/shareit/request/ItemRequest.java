package ru.practicum.shareit.request;

import lombok.Data;

import java.util.Date;

@Data
public class ItemRequest {
    String name;
    Integer id;
    String disc;
    String userRequestId;
    Date dateOfRequest;
}
