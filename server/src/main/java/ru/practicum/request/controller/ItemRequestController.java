package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.pageable.ConvertPageable;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.service.ItemRequestService;

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
    public ItemRequestDto addItemRequest(@RequestBody ItemRequestDto request,
                                         @RequestHeader("X-Sharer-User-Id") long id) {
        log.info("Создание запроса по id: {}", id);
        return service.addItemRequest(request, id);
    }
}
