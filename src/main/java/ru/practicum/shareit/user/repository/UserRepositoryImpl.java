package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> listUsers() {
        log.info("Количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public Long findUserById(long id) {
        long lastId = users.values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }

    @Override
    public User addUser(User user) {
        if (validation(user)) {
            long ids = findUserById(user.getId());
            user.setId(ids);
            users.put(ids, user);
            log.info("Пользователь создан: {}", user);
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (validation(user)) {
            users.put(user.getId(), user);
            log.info("Пользователь обновлён: {}", user);
        }
        return user;
    }

    @Override
    public boolean deleteUser(long id, User user) {
        if (validation(user)) {
            if (users.containsKey(user.getId())) {
                users.remove(user.getId());
                log.info("Пользователь удалён: {}", user);
            }
        }
        return false;
    }

    private boolean validation(User user) { // Обработка ошибок
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым или не содержит символ: @");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Имя не должно быть пустым, иначе будет использован логин!");
        }
        return false;
    }
}