package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.markers.Create;
import ru.practicum.shareit.markers.Update;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemServiceImpl;

    @PostMapping
    public ResponseEntity<ItemDto> addItem(@RequestHeader ("X-Sharer-User-Id") Integer userId, @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemServiceImpl.addItem(itemDto, userId), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Integer itemId) {
       return new ResponseEntity<>(itemServiceImpl.getItem(itemId), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader ("X-Sharer-User-Id") Integer userId, @RequestBody ItemDto itemDto, @Validated({Update.class}) @PathVariable Integer itemId) {
        return new ResponseEntity<>(itemServiceImpl.updateItem(itemDto, itemId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> allPersonalItems(@RequestHeader ("X-Sharer-User-Id") Integer userId) {
        return new ResponseEntity<>(itemServiceImpl.personalItems(userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam(defaultValue = "±") String text) {
        return new ResponseEntity<>(itemServiceImpl.search(text),HttpStatus.OK);
    }
}
