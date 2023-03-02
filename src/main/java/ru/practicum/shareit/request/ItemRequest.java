package ru.practicum.shareit.request;

import lombok.Data;

import java.util.Date;

@Data
public class ItemRequest {
    private String name;
    private Integer id;
    private String disc;
    private String userRequestId;
    private Date dateOfRequest;
}
