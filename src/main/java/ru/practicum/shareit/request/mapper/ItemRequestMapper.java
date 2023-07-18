package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;


public interface ItemRequestMapper {
    static ItemRequestDto toItemRequestDto(ItemRequest request) {
        List<ItemDto> items = request.getItems() == null ? null : request.getItems().stream()
                .map(ItemMapper::toItemRequest)
                .collect(Collectors.toList());

        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestorId(request.getRequestor().getId())
                .created(request.getCreated())
                .items(items).build();
    }

    static ItemRequest toItemRequest(ItemRequestDto request, User user) {
        if (request == null) {
            return null;
        }
        return ItemRequest.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestor(user)
                .created(request.getCreated()).build();
    }

    static List<ItemRequestDto> toListItemRequestDto(List<ItemRequest> requests) {
        return requests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }
}