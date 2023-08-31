package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemServiceImpl;

    @PostMapping
    public ResponseEntity<ItemDto> addItem(@RequestHeader ("X-Sharer-User-Id") Integer userId,
                                           @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemServiceImpl.addItem(itemDto, userId), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItem(@RequestHeader ("X-Sharer-User-Id") Integer userId,
                                           @PathVariable Integer itemId) {
       return new ResponseEntity<>(itemServiceImpl.getItemById(itemId, userId), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader ("X-Sharer-User-Id") Integer userId,
                                              @RequestBody ItemDto itemDto,
                                              @PathVariable Integer itemId) {
        return new ResponseEntity<>(itemServiceImpl.updateItem(itemDto, itemId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> allPersonalItems(@RequestHeader ("X-Sharer-User-Id") Integer userId,
                                                          @RequestParam(value = "from", defaultValue = "0", required = false)
                                                          Integer offset,
                                                          @RequestParam(value = "size", defaultValue = "10", required = false)
                                                          Integer limit) {
        return new ResponseEntity<>(itemServiceImpl.personalItems(userId, offset, limit), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam(defaultValue = "±") String text,
                                                @RequestParam(value = "from", defaultValue = "0", required = false)
                                                Integer offset,
                                                @RequestParam(value = "size", defaultValue = "10", required = false)
                                                Integer limit) {
        return new ResponseEntity<>(itemServiceImpl.search(text, offset, limit),HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@PathVariable Integer itemId,
                                                 @RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(itemServiceImpl.addComment(itemId, userId, commentDto), HttpStatus.OK);
    }
}
