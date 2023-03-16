package ru.practicum.shareit.item.dto;

import org.mapstruct.*;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toItemDto(Item item);

    @InheritInverseConfiguration
    Item toItem(ItemDto itemDto);

}
