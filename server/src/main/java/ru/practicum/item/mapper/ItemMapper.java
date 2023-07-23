package ru.practicum.item.mapper;


import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.mapper.ItemRequestMapper;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.user.mapper.UserMapper.toUserDto;


public interface ItemMapper {
    static ItemDto toItemDto(Item item) {
        Long requestId = item.getRequest() != null ? item.getRequest().getId() : null;
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(toUserDto(item.getOwner()))
                .available(item.getAvailable())
                .requestId(requestId).build();
    }

    static Item toItem(ItemDto item, User user, ItemRequestDto request) {
        return Item.builder()
                .id(item.getId() != null ? item.getId() : 0)
                .name(item.getName())
                .description(item.getDescription())
                .owner(user)
                .available(item.getAvailable())
                .request(ItemRequestMapper.toItemRequest(request, user)).build();
    }

    static List<ItemDto> toItemListDto(List<Item> item) {
        return item.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}