package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto findById(long id, long user);

    List<ItemRequestDto> getAllItemRequests(long id, Pageable page);

    List<ItemRequestDto> getItemRequest(long id);

    ItemRequestDto addItemRequest(ItemRequestDto request, long id);
}
