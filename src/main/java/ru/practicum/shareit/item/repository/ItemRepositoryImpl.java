package ru.practicum.shareit.item.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final UserRepository repository;

    @Override
    public Item findItemById(long id) {
        return items.get(id);
    }

    @Override
    public List<Item> findUserById(long id) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchText(String text) {
        String str = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.isAvailable()
                        && item.getName().toLowerCase().contains(str)
                        || item.getDescription().toLowerCase().contains(str))
                .collect(Collectors.toList());
    }

    @Override
    public Item addItem(Item item, int id) {
        if (validation(item)) {
            item.setOwner(mapUser().get(id));
            long ids = item.getId();
            item.setId(ids);
            items.put(ids, item);
            log.info("Предмет создан: {}", item);
        }
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        if (validation(item)) {
            items.put(item.getId(), item);
            log.info("Предмет обновлён: {}", item);
        }
        return item;
    }

    @Override
    public boolean deleteItem(long id, Item item) {
        if (validation(item)) {
            if (items.containsKey(item.getId())) {
                items.remove(item.getId());
                log.info("Пользователь удалён: {}", item);
            }
        }
        return false;
    }

    private List<User> mapUser() {
        return repository.listUsers();
    }

    private boolean validation(Item item) { // Обработка ошибок
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Имя не должно быть пустым, иначе будет использован логин!");
        }
        if (item.getDescription().length() > 200) {
            throw new ValidationException("Не может содержать больше 200 символов!");
        }
        return false;
    }
}
