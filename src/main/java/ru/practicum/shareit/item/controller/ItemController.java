package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @GetMapping("/{id}")
    public Item findItemById(@RequestHeader("X-Shareit-User-Id") long id) {
        return service.findItemById(id);
    }

    @GetMapping
    public List<Item> findUserById(@RequestHeader("X-Shareit-User-Id") long id) {
        return service.findUserById(id);
    }

    @GetMapping("/search")
    public List<Item> searchText(String text) {
        return service.searchText(text);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Item addItem(@RequestHeader("X-Shareit-User-Id") @Valid @RequestBody Item item, int id) {
        return service.addItem(item, id);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Item updateItem(@RequestHeader("X-Shareit-User-Id") @Valid @RequestBody Item item) {
        return service.updateItem(item);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable long id, Item item) {
        return service.deleteItem(id, item);
    }
}
