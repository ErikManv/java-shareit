package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingLinkDto;
import ru.practicum.shareit.booking.dto.BookingLinkDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.Status.REJECTED;

@Service
@RequiredArgsConstructor
@Primary
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    private final BookingLinkDtoMapper bookingLinkMapper;

    private final ItemMapper itemMapper;

    private final CommentMapper commentMapper;

    private final ItemRequestRepository itemRequestRepository;

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @Override
    public ItemDto addItem(ItemDto itemDto, Integer userId) {
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(getUser(userId));
        if (itemDto.getRequestId() != null) {
            item.setRequest(getItemRequest(itemDto.getRequestId()));
        }
        itemRepository.save(item);
        log.info("предмет {} добавлен", itemDto.getName());
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemUp, Integer itemId, Integer userId) {
        if (!getItem(itemId).getOwner().getId().equals(userId)) {
            log.error("не совпадает с id {} владельца", userId);
            throw new UserValidationException("не совпадает с id владельца");
        }
        Item item = getItem(itemId);
        if (itemUp.getName() != null) item.setName(itemUp.getName());
        if (itemUp.getDescription() != null) item.setDescription(itemUp.getDescription());
        if (itemUp.getAvailable() != null) item.setAvailable(itemUp.getAvailable());
        log.info("предмет {} обновлен", itemId);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Integer itemId, Integer userId) {
        Item item = getItem(itemId);
        log.info("предмет {} получен", itemId);
        if (item.getOwner().getId().equals(userId)) {
            return setCommentToItem(setBookingToItem(itemMapper.toItemDto(getItem(itemId))));
        }
        return setCommentToItem(itemMapper.toItemDto(getItem(itemId)));
    }

    @Override
    public List<ItemDto> personalItems(Integer userId, Integer offset, Integer limit) {
        if ((offset + 1) % limit == 0) {
            offset = ((offset + 1) / limit) - 1;
        } else if ((offset + 1) % limit != 0) {
            offset = ((offset + 1) / limit);
        }
        List<ItemDto> itemList = new ArrayList<>();
        for (Item i: itemRepository.findByOwner(getUser(userId), PageRequest.of(offset, limit, Sort.by("id").ascending()))) {
            itemList.add(setCommentToItem(setBookingToItem(itemMapper.toItemDto(i))));
        }
        log.info("список для пользователя {} получен", userId);
        return itemList.stream().sorted(Comparator.comparing(ItemDto::getId)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text, Integer offset, Integer limit) {
        if ((offset + 1) % limit == 0) {
            offset = ((offset + 1) / limit) - 1;
        } else if ((offset + 1) % limit != 0) {
            offset = ((offset + 1) / limit);
        }
        log.info("поиск завершен");
        String textNew = text.toLowerCase().trim();
        return itemRepository.findItemByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(textNew, textNew, PageRequest.of(offset, limit, Sort.by("id").ascending())).stream()
                .filter(Item::getAvailable)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto setBookingToItem(ItemDto itemDto) {
        itemDto.setLastBooking(getLastAndNextItemBookings(itemDto).get(0));
        itemDto.setNextBooking(getLastAndNextItemBookings(itemDto).get(1));
        return itemDto;
    }

    @Override
    public ItemDto setCommentToItem(ItemDto itemDto) {
        itemDto.setComments(getItemComments(itemDto));
        return itemDto;
    }

    @Override
    public CommentDto addComment(Integer itemId, Integer userId, CommentDto commentDto) {
        Comment comment = new Comment();
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findAllByItem_IdAndBooker_Id(itemId, userId);
        if (!bookings.isEmpty() && bookings.stream()
                .anyMatch(x -> !x.getStatus().equals(REJECTED) && x.getEnd().isBefore(now))) {
            comment.setText(commentDto.getText());
            comment.setItem(getItem(itemId));
            comment.setAuthor(getUser(userId));
            comment.setCreated(now);
            commentRepository.save(comment);
        } else {
            throw new CommentWithoutBookingException("not");
        }
        return commentMapper.toDto(comment);
    }

    private List<BookingLinkDto> getLastAndNextItemBookings(ItemDto itemDto) {
        List<BookingLinkDto> lastAndNextBookings = new ArrayList<>();
        lastAndNextBookings.add(0, null);
        lastAndNextBookings.add(1, null);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> itemBookings = bookingRepository.findAllByItem_Id(itemDto.getId());
        if (itemBookings.size() == 1) {
            if (itemBookings.get(0).getStart().isBefore(now) && !itemBookings.get(0).getStatus().equals(REJECTED)) {
                lastAndNextBookings.add(0, bookingLinkMapper.toDto(itemBookings.get(0)));
                return lastAndNextBookings;
            } else if (itemBookings.get(0).getStart().isAfter(now) && !itemBookings.get(0).getStatus().equals(REJECTED)) {
                lastAndNextBookings.add(1, bookingLinkMapper.toDto(itemBookings.get(0)));
                return lastAndNextBookings;
            }
        }
        lastAndNextBookings.add(0, bookingLinkMapper.toDto(itemBookings.stream()
                .filter(x -> !x.getStatus().equals(REJECTED))
                .filter(x -> x.getEnd().isBefore(now)).max(Comparator.comparing(Booking::getStart)).orElse(null)));
        lastAndNextBookings.add(1, bookingLinkMapper.toDto(itemBookings.stream()
                .filter(x -> !x.getStatus().equals(REJECTED))
                .filter(x -> x.getStart().isAfter(now)).min(Comparator.comparing(Booking::getStart)).orElse(null)));
        return lastAndNextBookings;
    }

    private List<CommentDto> getItemComments(ItemDto itemDto) {
        return commentRepository.findAllByItem_Id(itemDto.getId()).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    private Item getItem(Integer itemId) {
        return itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFoundException("Item id=%s не найден"));
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User id=%s не найден"));
    }

    private ItemRequest getItemRequest(Integer itemRequestId) {
        return itemRequestRepository.findById(itemRequestId)
            .orElseThrow(() -> new ItemNotFoundException("ItemRequest id=%s не найден"));
    }
}
