package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient client;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return client.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable long id) {
        log.info("Пользователь с id: {}", id);
        return client.findById(id);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto user) {
        log.info("Пользователь {} успешно создан!", user);
        return client.addUser(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto user, @PathVariable long id) {
        log.info("Пользователь с id: {} успешно обновлён!", id);
        return client.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.info("Пользователь с id: {} удалён!", id);
        client.deleteUser(id);
    }
}