package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class ItemStorage {

    private Integer itemId = 0;

    private void countId() {
        itemId++;
    }

    private final Map<Integer, Item> items = new HashMap<>();

    public ItemDto addItem(ItemDto itemDto, User user) {
        countId();
        itemDto.setId(itemId);
        Item item = ItemMapper.toItem(itemDto, user);
        items.put(itemDto.getId(), item);
        return ItemMapper.toItemDto(item);
    }

    public ItemDto updateItem(ItemDto itemUp, Integer itemId) {
        Item item = items.get(itemId);
        if(itemUp.getName() != null) item.setName(itemUp.getName());
        if(itemUp.getDescription() != null) item.setDescription(itemUp.getDescription());
        if(itemUp.getAvailable() != null) item.setAvailable(itemUp.getAvailable());
        return ItemMapper.toItemDto(item);
    }

    public Item getItem(Integer itemId) {      // так как в ItemService применяется этот метод, тут он возвращает Item, а в серивсе ItemDto
        return items.get(itemId);
    }

    public List<ItemDto> personalItems(Integer userId) {
        List<ItemDto> itemList = new ArrayList<>();
        for(Item item: items.values()) {
            if(Objects.equals(item.getOwner().getId(), userId)) {
                itemList.add(ItemMapper.toItemDto(item));
            }
        }
        return itemList;
    }

    public List<ItemDto> search(String text) {
        String textNew = text.toLowerCase().trim();
        List<ItemDto> itemList = new ArrayList<>();
        for(Item item: items.values()) {
            if(item.getAvailable() &&
                    (item.getName().toLowerCase().contains(textNew)
                            || item.getDescription().toLowerCase().contains(textNew))) {
                itemList.add(ItemMapper.toItemDto(item));
            }
        }
        return itemList;
    }
}
