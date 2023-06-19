package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UserExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public List<User> listUsers() {
        log.info("Количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Не найдено!");
        }
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        if (users.values().stream()
                .anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new UserExistsException("Пользователь уже существует!");
        }
        user.setId(id + 1);
        users.put(user.getId(), user);
        id++;
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user, long id) {
        if (users.containsKey(id)) {
            User update = User.builder()
                    .id(id)
                    .name(user.getName() != null ? user.getName() : users.get(id).getName())
                    .email(user.getEmail() != null ? user.getEmail() : users.get(id).getEmail())
                    .build();
            if (users.values().stream()
                    .anyMatch(user1 -> user1.getEmail().equals(user.getEmail())
                            && !Objects.equals(user1.getId(), id))) {
                throw new UserExistsException("Пользователь уже существует!");
            }
            users.put(id, update);
            log.info("Пользователь обновлён: {}", user);
            return update;
        }
        return user;
    }

    @Override
    public boolean deleteUser(long id) {
        if (users.containsKey(id)) {
            users.remove(id);
            log.info("Пользователь удалён: {}", id);
        }
        return false;
    }
}