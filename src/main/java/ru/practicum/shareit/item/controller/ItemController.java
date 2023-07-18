package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.pageable.ConvertPageable;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @GetMapping("/{id}")
    public ItemDto findById(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long user) {
        log.info("Поиск предмета по его id: {}", id);
        return service.findById(id, user);
    }

    @GetMapping
    List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") long id,
                              @RequestParam(required = false) Integer from,
                              @RequestParam(required = false) Integer size) {
        log.info("Получение списка всех вещей пользователя");
        return service.getItems(id, ConvertPageable.toMakePage(from, size));
    }

    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") long user,
                                      @RequestParam(required = false) Integer from,
                                      @RequestParam(required = false) Integer size) {
        log.info("Поиск предмет по запросу: {}", text);
        return service.searchByText(text, user, ConvertPageable.toMakePage(from, size));
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") long id) {
        log.info("Предмет с id: {} успешно создан!", id);
        return service.addItem(item, id);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemDto item, @PathVariable long id,
                              @RequestHeader("X-Sharer-User-Id") long user) {
        log.info("Предмет с id: {} успешно обновлён!", id);
        return service.updateItem(item, id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable long id) {
        log.info("Предмет с id: {} удалён!", id);
        service.deleteItem(id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@Valid @RequestBody CommentDto comment, @PathVariable long itemId,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Комментарий к предмету: {} создан!", itemId);
        return service.addComment(comment, itemId, userId);
    }
}