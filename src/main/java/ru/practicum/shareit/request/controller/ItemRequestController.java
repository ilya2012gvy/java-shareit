package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.pageable.ConvertPageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @GetMapping("/{id}")
    public ItemRequestDto findById(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long user) {
        log.info("запрос пользователя: {} с id: {}", user, id);
        return service.findById(id, user);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") long id,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        log.info("Все запросы по id: {}", id);
        return service.getAllItemRequests(id, ConvertPageable.toMakePage(from, size));
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequest(@RequestHeader("X-Sharer-User-Id") long id) {
        log.info("Список запросов пользователя с id: {}", id);
        return service.getItemRequest(id);
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@Valid @RequestBody ItemRequestDto request,
                                         @RequestHeader("X-Sharer-User-Id") long id) {
        log.info("Создание запроса по id: {}", id);
        return service.addItemRequest(request, id);
    }
}
