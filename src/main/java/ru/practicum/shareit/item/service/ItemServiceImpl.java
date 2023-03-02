package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dao.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public ItemDto addItem(ItemDto itemDto, Integer userId) {
        containsUser(userId);
        log.info("предмет {} добавлен", itemDto.getName());
        return itemStorage.addItem(itemDto, userStorage.getUser(userId));
    }

    @Override
    public ItemDto updateItem(ItemDto itemUp, Integer itemId, Integer userId) {
        containsUser(userId);
        containsItem(itemId);
        if (!itemStorage.getItem(itemId).getOwner().getId().equals(userId)) {
            log.error("не совпадает с id {} владельца", userId);
            throw new UserValidationException("не совпадает с id владельца");
        }
        log.info("предмет {} обновлен", itemId);
        return itemStorage.updateItem(itemUp, itemId);
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        containsItem(itemId);
        log.info("предмет {} получен", itemId);
        return ItemMapper.INSTANCE.toItemDto(itemStorage.getItem(itemId));
    }

    @Override
    public List<ItemDto> personalItems(Integer userId) {
        containsUser(userId);
        log.info("список для пользователя {} получен", userId);
        return itemStorage.personalItems(userId);
    }

    @Override
    public List<ItemDto> search(String text) {
        log.info("поиск завершен");
        return itemStorage.search(text);
    }

    private void containsUser(Integer userId) {
        if (userStorage.getUser(userId) == null) {
            log.error("пользователя с id {} не существует", userId);
            throw new UserValidationException("пользователя не существует");
        }
    }

    private void containsItem(Integer itemId) {
        if (itemStorage.getItem(itemId) == null) {
            log.error("предмета с id {} не существует", itemId);
            throw new ItemValidationException("предмет не существует");
        }
    }
}
