package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserDto> listUsers() {
        return service.listUsers();
    }

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable long id) {
        return service.findUserById(id);
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto user) {
        return service.addUser(user);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto user, @PathVariable long id) {
        return service.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable long id) {
        return service.deleteUser(id);
    }
}