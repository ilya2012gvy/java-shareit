package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


public interface ItemService {

    ItemDto findById(long id, long user);

    List<ItemDto> getItems(long id, Pageable page);

    List<ItemDto> searchByText(String text, long user, Pageable page);

    ItemDto addItem(ItemDto item, long id);

    ItemDto updateItem(ItemDto item, long id, long user);

    void deleteItem(long id);

    CommentDto addComment(CommentDto comment, long id, long user);
}