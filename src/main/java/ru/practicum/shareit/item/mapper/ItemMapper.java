package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public interface ItemMapper {
    static ItemDto toItemDto(Item item, BookingRequestDto last, BookingRequestDto next, List<CommentDto> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(last)
                .nextBooking(next)
                .comments(comments).build();
    }

    static Item toItem(ItemDto item, User user) {
        return Item.builder()
                .id(item.getId() != null ? item.getId() : 0)
                .name(item.getName())
                .description(item.getDescription())
                .owner(user)
                .available(item.getAvailable()).build();
    }

    static List<ItemDto> toItemListDto(List<Item> item) {
        return item.stream()
                .map(items -> ItemMapper.toItemDto(items, null, null, null))
                .collect(Collectors.toList());
    }
}