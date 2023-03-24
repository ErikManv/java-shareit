package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    Page<Item> findItemByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String textN, String textD, Pageable pageable);

    List<Item> findByOwner(User user);

    Page<Item> findByOwner(User user, Pageable pageable);

    List<Item> findByRequestId(Integer requestId);
}
