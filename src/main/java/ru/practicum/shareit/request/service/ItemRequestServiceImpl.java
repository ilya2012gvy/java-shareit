package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.request.mapper.ItemRequestMapper.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto findById(long id, long user) {
        userRepository.findById(user).orElseThrow(() ->
                new UserNotFoundException("ItemRequestServiceImpl: User findById Not Found 404"));
        ItemRequest request = requestRepository.findById(id).orElseThrow(() ->
                new ItemRequestNotFoundException("ItemRequestServiceImpl: ItemRequest getRequests Not Found 404"));
        return toItemRequestDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllItemRequests(long id, Pageable page) {
        userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("ItemRequestServiceImpl: User getRequests Not Found 404"));
        return toListItemRequestDto(requestRepository.findByIdNot(id, page));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getItemRequest(long id) {
        userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("ItemRequestServiceImpl: User getAllItemRequest Not Found 404"));
        return toListItemRequestDto(requestRepository.findByRequestorIdOrderByIdDesc(id));
    }

    @Override
    @Transactional
    public ItemRequestDto addItemRequest(ItemRequestDto request, long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("ItemRequestServiceImpl: User addRequest Not Found 404"));
        request.setCreated(LocalDateTime.now());
        return toItemRequestDto(requestRepository.save(toItemRequest(request, user)));
    }
}
