package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Service
public interface ItemRequestService {

    ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Integer userId);

    List<ItemRequestDto> getOwnRequests(Integer userId);

    List<ItemRequestDto> getAllOthersRequests(Integer userId, Integer offset, Integer limit);

    ItemRequestDto getRequestById(Integer requestId, Integer userId);
}
