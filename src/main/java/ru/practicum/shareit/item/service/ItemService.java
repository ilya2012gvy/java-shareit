package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto findItemById(long id);

    List<ItemDto> findUserById(long id);

    List<ItemDto> searchText(String text);

    ItemDto addItem(ItemDto item, long id);

    ItemDto updateItem(ItemDto item, long id, long user);

    boolean deleteItem(long id);
}
