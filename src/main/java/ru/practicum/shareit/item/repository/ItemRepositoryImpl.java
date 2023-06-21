package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final UserRepository repository;
    private long itemId = 0;

    @Override
    public Item findItemById(long id) {
        if (!items.containsKey(id)) {
            throw new ItemNotFoundException("Не найдено!");
        }
        return items.get(id);
    }

    @Override
    public List<Item> getAllItems(long id) {
        return items.values().stream()
                .filter(item -> item.getOwner() == id)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchByText(String text) {
        if (!text.isEmpty()) {
            String str = text.toLowerCase();
            return items.values().stream()
                    .filter(item -> item.getName().toLowerCase().contains(str)
                            || item.getDescription().toLowerCase().contains(str)
                            && item.getAvailable())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public Item addItem(Item item, long id) {
        validation(id);
        item.setId(itemId + 1);
        item.setOwner(id);
        items.put(item.getId(), item);
        itemId++;
        log.info("Предмет создан: {}", item);
        return item;
    }

    @Override
    public Item updateItem(Item item, long id, long user) {
        validation(user);
        if (items.containsKey(id)) {
            if (item.getAvailable() == null) {
                item.setAvailable(true);
            }
            Item update = Item.builder()
                    .id(id)
                    .name(item.getName() != null ? item.getName() : items.get(id).getName())
                    .description(item.getDescription() != null ? item.getDescription() : items.get(id).getDescription())
                    .available(item.getAvailable() != null ? item.getAvailable() : items.get(id).getAvailable())
                    .owner(item.getOwner() != null ? item.getOwner() : items.get(id).getOwner())
                    .build();
            if (!Objects.equals(items.get(id).getOwner(), user)) {
                throw new ItemNotFoundException("Предмет уже существует!");
            }
            items.put(id, update);
            log.info("Предмет обновлён: {}", update);
            return update;
        }
        return item;
    }

    @Override
    public boolean deleteItem(long id) {
        items.remove(id);
        log.info("Предмет удалён: {}", id);
        return items.containsKey(id);
    }

    private void validation(long id) {
        repository.findById(id);
    }
}
