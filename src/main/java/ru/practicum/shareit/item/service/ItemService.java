package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto findItemById(long id);

    List<ItemDto> findItemByUserId(long id);

    List<ItemDto> searchByText(String text);

    ItemDto addItem(ItemDto item, long id);

    ItemDto updateItem(ItemDto item, long id, long user);

    boolean deleteItem(long id);
}
