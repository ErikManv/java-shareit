package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
public interface ItemService {

    ItemDto addItem(ItemDto itemDto, Integer userId);

    ItemDto updateItem(ItemDto itemUp, Integer itemId, Integer userId);

    ItemDto getItemById(Integer itemId, Integer userId);

    List<ItemDto> personalItems(Integer userId, Integer offset, Integer limit);

    List<ItemDto> search(String text, Integer offset, Integer limit);

    ItemDto setBookingToItem(ItemDto itemDto);

    ItemDto setCommentToItem(ItemDto itemDto);

    CommentDto addComment(Integer itemId, Integer userId, CommentDto commentDto);
}
