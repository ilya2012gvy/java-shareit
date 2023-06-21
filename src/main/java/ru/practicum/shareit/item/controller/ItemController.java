package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @GetMapping("/{id}")
    public ItemDto findItemById(@PathVariable long id) {
        log.info("Поиск предмета по его id: {}", id);
        return service.findItemById(id);
    }

    @GetMapping
    public List<ItemDto> findItemByUserId(@RequestHeader("X-Sharer-User-Id") long id) {
        log.info("Поиск предмет по id пользователя: {}", id);
        return service.findItemByUserId(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestParam String text) {
        log.info("Поиск предмет по запросу: {}", text);
        return service.searchByText(text);
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") long id) {
        log.info("Предмет успешно создан!");
        return service.addItem(item, id);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemDto item, @PathVariable long id,
                              @RequestHeader("X-Sharer-User-Id") long user) {
        log.info("Предмет успешно обновлён!");
        return service.updateItem(item, id, user);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable long id) {
        log.info("Предмет удалён!");
        return service.deleteItem(id);
    }
}