package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.markers.Create;
import ru.practicum.shareit.markers.Update;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    ItemServiceImpl itemServiceImpl;
    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader ("X-Sharer-User-Id") Integer userId, @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        return itemServiceImpl.addItem(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Integer itemId) {
       return itemServiceImpl.getItem(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader ("X-Sharer-User-Id") Integer userId, @RequestBody ItemDto itemDto, @Validated({Update.class}) @PathVariable Integer itemId) {
        return itemServiceImpl.updateItem(itemDto, itemId, userId);
    }

    @GetMapping
    public List<ItemDto> allPersonalItems(@RequestHeader ("X-Sharer-User-Id") Integer userId) {
        return itemServiceImpl.personalItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(defaultValue = "±") String text) {
        return itemServiceImpl.search(text);
    }
}
