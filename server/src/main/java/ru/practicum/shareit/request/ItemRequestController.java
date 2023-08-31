package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
  * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> addItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                         @RequestBody ItemRequestDto itemRequestDto) {
        return new ResponseEntity<>(itemRequestService.addItemRequest(itemRequestDto, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getAllOwnItemRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return new ResponseEntity<>(itemRequestService.getOwnRequests(userId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequestsOfUsers(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                          @RequestParam(value = "from", defaultValue = "0", required = false)
                                                          Integer offset,
                                                          @RequestParam(value = "size", defaultValue = "10", required = false)
                                                          Integer limit) {
        return itemRequestService.getAllOthersRequests(userId, offset, limit);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @PathVariable Integer requestId) {
        return itemRequestService.getRequestById(requestId, userId);
    }
}
