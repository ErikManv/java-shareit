package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(target = "requesterId", source = "owner.id")
    ItemRequestDto toDto(ItemRequest model);

    ItemRequest toModel(ItemRequestDto dto);
}
