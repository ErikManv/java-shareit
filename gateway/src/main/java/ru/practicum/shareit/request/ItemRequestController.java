package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnItemRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestClient.getAllOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOthersRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                     @RequestParam(value = "from", defaultValue = "0",
                                                         required = false) @Min(0) Integer from,
                                                     @RequestParam(value = "size", defaultValue = "10",
                                                         required = false) @Min(1) @Max(50) Integer size) {
        return itemRequestClient.getAllOthersRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @PathVariable Integer requestId) {
        return itemRequestClient.getItemRequestById(userId, requestId);
    }
}
