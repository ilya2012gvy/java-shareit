package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient client;

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long user) {
        log.info("Поиск предмета по его id: {}", id);
        return client.findById(id, user);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader("X-Sharer-User-Id") long id,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение списка всех вещей пользователя");
        return client.getAllItems(id, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByText(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") long user,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Поиск предмет по запросу: {}", text);
        return client.searchByText(text, user, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") long id) {
        log.info("Предмет с id: {} успешно создан!", id);
        return client.addItem(item, id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto item, @PathVariable long id,
                                             @RequestHeader("X-Sharer-User-Id") long user) {
        log.info("Предмет с id: {} успешно обновлён!", id);
        return client.updateItem(item, id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable long id) {
        log.info("Предмет с id: {} удалён!", id);
        client.deleteItem(id);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto comment, @PathVariable long itemId,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Комментарий к предмету: {} создан!", itemId);
        return client.addComment(comment, itemId, userId);
    }
}