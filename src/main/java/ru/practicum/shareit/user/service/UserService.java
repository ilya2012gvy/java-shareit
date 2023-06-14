package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    List<User> listUsers();

    Long findUserById(long id);

    User addUser(User user);

    User updateUser(User user);

    boolean deleteUser(long id, User user);
}
