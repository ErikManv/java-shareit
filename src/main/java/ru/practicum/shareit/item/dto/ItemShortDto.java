package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemShortDto {
    private Integer id;

    private String name;

    private String description;

    private Integer ownerId;

    private Boolean available;

    private Integer requestId;
}
