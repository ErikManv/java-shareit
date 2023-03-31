package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    User user = User.builder()
        .id(1)
        .name("user")
        .email("user@test.ru")
        .build();
    User user1 = User.builder()
        .id(2)
        .name("user1")
        .email("user1@test.ru")
        .build();

    @BeforeAll
    public void addUsers() {
        userRepository.save(user);
        userRepository.save(user1);
    }

    @Test
    void update() {
        User userUp = User.builder()
            .name("update")
            .email("update@test.ru")
            .id(1)
            .build();
        userRepository.save(userUp);
        User updatedUser = userRepository.findById(1).get();
        assertEquals(userUp.getName(), updatedUser.getName());
        assertEquals(userUp.getEmail(), updatedUser.getEmail());
    }

    @AfterAll
    public void deleteUsers() {
        userRepository.deleteAll();
    }
}
