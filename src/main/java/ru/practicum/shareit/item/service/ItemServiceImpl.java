package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public ItemDto findItemById(long id) {
        return toItemDto(repository.findItemById(id));
    }

    @Override
    public List<ItemDto> getAllItems(long id) {
        return repository.getAllItems(id).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchByText(String text) {
        return repository.searchByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto addItem(ItemDto item, long id) {
        return toItemDto(repository.addItem(toItem(item), id));
    }

    @Override
    public ItemDto updateItem(ItemDto item, long id, long user) {
        return toItemDto(repository.updateItem(toItem(item), id, user));
    }

    @Override
    public boolean deleteItem(long id) {
        return repository.deleteItem(id);
    }
}