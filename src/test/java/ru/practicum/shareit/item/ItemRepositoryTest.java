package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemRepository itemRepository;

    User owner = User.builder()
        .id(1)
        .name("Item owner")
        .email("item_owner@test.ru")
        .build();

    User requester = User.builder()
        .id(2)
        .name("Item requestor")
        .email("item_requestor@test.ru")
        .build();

    ItemRequest itemRequest = ItemRequest.builder()
        .id(1)
        .owner(requester)
        .description("item request")
        .created(LocalDateTime.now())
        .build();
    Item item = Item.builder()
        .id(1)
        .owner(owner)
        .description("test item")
        .available(true)
        .name("item")
        .build();

    Item item1 = Item.builder()
        .id(2)
        .owner(owner)
        .description("test item1")
        .available(true)
        .name("item1")
        .request(itemRequest)
        .build();

    @BeforeAll
    public void addUsers() {
        userRepository.save(owner);
        userRepository.save(requester);
        itemRequestRepository.save(itemRequest);
        itemRepository.save(item);
        itemRepository.save(item1);
    }

    @Test
    void update() {
        Item itemUp = Item.builder()
            .id(2)
            .owner(requester)
            .description("updated description")
            .available(false)
            .name("updated name")
            .build();
        itemRepository.save(itemUp);
        Item updatedItem = itemRepository.findById(2).get();
        assertEquals(itemUp.getOwner().getId(), updatedItem.getOwner().getId());
        assertEquals(itemUp.getName(), updatedItem.getName());
        assertEquals(itemUp.getDescription(), updatedItem.getDescription());
        assertEquals(itemUp.getAvailable(), updatedItem.getAvailable());
    }

    @AfterAll
    public void deleteItems() {
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }
}