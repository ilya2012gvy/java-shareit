package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> listUsers();

    UserDto findUserById(long id);

    UserDto addUser(UserDto user);

    UserDto updateUser(UserDto user, long id);

    boolean deleteUser(long id);
}
