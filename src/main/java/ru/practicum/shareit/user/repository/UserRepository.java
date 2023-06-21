package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User findById(long id);

    User addUser(User user);

    User updateUser(User user, long id);

    boolean deleteUser(long id);
}
