package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item findItemById(long id);

    List<Item> findUserById(long id);

    List<Item> searchText(String text);

    Item addItem(Item item, int id);

    Item updateItem(Item item);

    boolean deleteItem(long id, Item item);
}