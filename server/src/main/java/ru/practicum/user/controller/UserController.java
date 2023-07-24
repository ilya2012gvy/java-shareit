package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserDto> findAll() {
        List<UserDto> all = service.findAll();
        log.info("Количество пользователей: {}", all);
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable long id) {
        log.info("Пользователь с id: {}", id);
        return service.findById(id);
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto user) {
        log.info("Пользователь {} успешно создан!", user);
        return service.addUser(user);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto user, @PathVariable long id) {
        log.info("Пользователь с id: {} успешно обновлён!", id);
        return service.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.info("Пользователь с id: {} удалён!", id);
        service.deleteUser(id);
    }
}