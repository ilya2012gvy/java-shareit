package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto findById(long id);

    UserDto addUser(UserDto user);

    UserDto updateUser(UserDto user, long id);

    void deleteUser(long id);
}
