package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager entity;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        User owner = User.builder()
                .id(1L)
                .email("John.doe@mail.com")
                .name("John").build();

        User user = userRepository.save(owner);

        Item item = Item.builder()
                .name("ОтВёртка маленЬкая")
                .description("Отвёртка")
                .available(true)
                .owner(user)
                .build();

        Item item2 = Item.builder()
                .name("Дрель Аккумуляторная")
                .description("Большая")
                .available(true)
                .owner(user)
                .build();

        Item item3 = Item.builder()
                .name("Молоток")
                .description("Молоток")
                .available(false)
                .owner(user)
                .build();

        itemRepository.save(item);
        itemRepository.save(item2);
        itemRepository.save(item3);
    }

    @Test
    void contextNotNull() {
        assertThat(entity).isNotNull();
    }

    @Test
    void searchByText() {
        List<Item> items = itemRepository.search("отвёртка", Pageable.unpaged());
        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getName(), "ОтВёртка маленЬкая");
    }

    @Test
    void searchByTextSearchNameAndDescription() {
        List<Item> items = itemRepository.search("Дрель", Pageable.unpaged());
        assertEquals(items.size(), 1);
    }

    @Test
    void searchByTextSearchNotAvailable() {
        List<Item> items = itemRepository.search("Молоток", Pageable.unpaged());
        assertEquals(items.size(), 0);
    }
}
