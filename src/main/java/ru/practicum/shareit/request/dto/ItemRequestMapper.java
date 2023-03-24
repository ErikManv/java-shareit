package ru.practicum.shareit.request.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ItemRequestMapper {

    @Mapping(target = "requesterId", source = "owner.id")
    ItemRequestDto toDto(ItemRequest model);

    ItemRequest toModel(ItemRequestDto dto);
}
