package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @RequestBody @Valid ItemDto itemDto) {
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @PathVariable Integer itemId,
                                             @RequestBody ItemDto itemDto) {
        return itemClient.updateItem(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                              @PathVariable Integer itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> allPersonalItems(@RequestHeader ("X-Sharer-User-Id") Integer userId,
                                                          @RequestParam(value = "from", defaultValue = "0", required = false)
                                                          @Min(0) Integer from,
                                                          @RequestParam(value = "size", defaultValue = "10", required = false)
                                                          @Min(1) @Max(50) Integer size) {
        return itemClient.allPersonalItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(defaultValue = "±") String text,
                                                @RequestParam(value = "from", defaultValue = "0", required = false)
                                                @Min(0) Integer from,
                                                @RequestParam(value = "size", defaultValue = "10", required = false)
                                                @Min(1) @Max(50) Integer size) {
        return itemClient.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Integer itemId,
                                             @RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @Valid @RequestBody CommentDto commentDto) {
        return itemClient.addComment(itemId, userId, commentDto);
    }
}
