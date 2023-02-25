package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
public class Item {
    private Integer id;
    private User owner;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
}
