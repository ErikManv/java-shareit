package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findItemByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String textN, String textD);

    List<Item> findByOwner(User user);
}
