package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto findById(long id, long user);

    List<ItemDto> getItems(long id);

    List<ItemDto> searchByText(String text, long user);

    ItemDto addItem(ItemDto item, long id);

    ItemDto updateItem(ItemDto item, long id, long user);

    void deleteItem(long id);

    CommentDto addComment(CommentDto comment, long id, long user);
}