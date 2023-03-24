package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemShortMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final ItemRequestMapper itemRequestMapper;

    private final ItemShortMapper itemShortMapper;

    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Integer userId) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setOwner(getUser(userId));
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Integer userId) {
        return itemRequestRepository.findByOwner(getUser(userId)).stream()
            .map(itemRequestMapper::toDto)
            .map(this::setItemsByRequest)
            .sorted(Comparator.comparing(ItemRequestDto::getCreated))
            .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllOthersRequests(Integer userId, Integer offset, Integer limit) {
        return itemRequestRepository.findAllByOwnerIdNot(userId,
            PageRequest.of(offset, limit, Sort.by("created").ascending())).stream()
            .map(itemRequestMapper::toDto)
            .map(this::setItemsByRequest)
            .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Integer requestId, Integer userId) {
        getUser(userId);
        ItemRequestDto itemRequestDto = itemRequestMapper.toDto(getItemRequest(requestId));
        setItemsByRequest(itemRequestDto);
        return itemRequestDto;
    }

    private ItemRequestDto setItemsByRequest(ItemRequestDto request) {
        request.setItems(itemRepository.findByRequestId(request.getId()).stream()
            .map(itemShortMapper::toDto)
            .collect(Collectors.toList()));
        return request;
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User id=%s не найден"));
    }

    private ItemRequest getItemRequest(Integer itemRequestId) {
        return itemRequestRepository.findById(itemRequestId)
            .orElseThrow(() -> new ItemNotFoundException("ItemRequest не найден"));
    }
}
