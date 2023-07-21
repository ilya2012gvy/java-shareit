package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplIntegrationTest {
    private final UserService userService;
    private final ItemService itemService;
    private UserDto user;

    @BeforeEach
    void setUp() {
        UserDto owner = UserDto.builder()
                .id(1L)
                .email("John.doe@mail.com")
                .name("John")
                .build();

        ItemDto item = ItemDto.builder()
                .name("ОтВёртка маленЬкая")
                .description("Отвёртка")
                .available(true)
                .owner(user)
                .build();

        ItemDto item2 = ItemDto.builder()
                .name("Дрель аккумуляторная")
                .description("Большая")
                .available(true)
                .owner(user)
                .build();

        ItemDto item3 = ItemDto.builder()
                .name("Молоток")
                .description("Молоток")
                .available(false)
                .owner(user)
                .build();

        user = userService.addUser(owner);
        itemService.addItem(item, user.getId());
        itemService.addItem(item2, user.getId());
        itemService.addItem(item3, user.getId());
    }

    @Test
    void searchByText() {
        List<ItemDto> items = itemService.searchByText("отвёртка", user.getId(), Pageable.unpaged());
        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getName(), "ОтВёртка маленЬкая");
    }

    @Test
    void searchByTextSearchNameAndDescription() {
        List<ItemDto> items = itemService.searchByText("Дрель", user.getId(), Pageable.unpaged());
        assertEquals(items.size(), 1);
    }

    @Test
    void searchByTextSearchNotAvailable() {
        List<ItemDto> items = itemService.searchByText("Молоток", user.getId(), Pageable.unpaged());
        assertEquals(items.size(), 0);
    }
}