package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public Item findItemById(long id) {
        return repository.findItemById(id);
    }

    @Override
    public List<Item> findUserById(long id) {
        return repository.findUserById(id);
    }

    @Override
    public List<Item> searchText(String text) {
        return repository.searchText(text);
    }

    @Override
    public Item addItem(Item item, int id) {
        return repository.addItem(item, id);
    }

    @Override
    public Item updateItem(Item item) {
        return repository.updateItem(item);
    }

    @Override
    public boolean deleteItem(long id, Item item) {
        return repository.deleteItem(id, item);
    }
}
