package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemMapper {
    static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest()).build();
    }

    static Item toItem(ItemDto item) {
        return Item.builder()
                .id(item.getId() != null ? item.getId() : 0)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest()).build();
    }
}