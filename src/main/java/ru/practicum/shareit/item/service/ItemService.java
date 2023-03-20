package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, Integer userId);

    ItemDto updateItem(ItemDto itemUp, Integer itemId, Integer userId);

    ItemDto getItemById(Integer itemId, Integer userId);

    List<ItemDto> personalItems(Integer userId);

    List<ItemDto> search(String text);

    ItemDto setBookingToItem(ItemDto itemDto);

    ItemDto setCommentToItem(ItemDto itemDto);

    CommentDto addComment(Integer itemId, Integer userId, CommentDto commentDto);
}
