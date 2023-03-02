package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, Integer userId);

    ItemDto updateItem(ItemDto itemUp, Integer itemId, Integer userId);

    ItemDto getItem(Integer itemId);

    List<ItemDto> personalItems(Integer userId);

    List<ItemDto> search(String text);
}
