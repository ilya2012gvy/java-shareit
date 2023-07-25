package ru.practicum.request;

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
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestClient client;

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long user) {
        log.info("запрос пользователя: {} с id: {}", user, id);
        return client.findById(id, user);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") long id,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Все запросы по id: {}", id);
        return client.getAllItemRequests(id, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") long id) {
        log.info("Список запросов пользователя с id: {}", id);
        return client.getItemRequest(id);
    }

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@Valid @RequestBody ItemRequestDto request,
                                                 @RequestHeader("X-Sharer-User-Id") long id) {
        log.info("Создание запроса по id: {}", id);
        return client.addItemRequest(request, id);
    }
}