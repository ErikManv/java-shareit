package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.markers.Create;
import ru.practicum.shareit.markers.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {

    @NotNull(groups = {Update.class})
    private Integer id;
    @NotBlank(groups = {Create.class})
    private String name;

    @NotBlank(groups = {Create.class})
    private String description;

    @NotNull(groups = {Create.class})
    private Boolean available;

}
