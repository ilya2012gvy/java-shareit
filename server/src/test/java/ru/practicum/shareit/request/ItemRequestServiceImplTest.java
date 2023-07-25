package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.exception.ItemRequestNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.repository.ItemRequestRepository;
import ru.practicum.request.service.ItemRequestService;
import ru.practicum.request.service.ItemRequestServiceImpl;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ru.practicum.request.mapper.ItemRequestMapper.toItemRequest;


@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {
    @Mock
    ItemRequestRepository repository;
    @Mock
    UserRepository userRepository;

    private ItemRequestService service;
    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        service = new ItemRequestServiceImpl(repository, userRepository);

        user = User.builder()
                .id(1L)
                .email("Jon.doe@mail.com")
                .name("Jon").build();

        itemRequest = ItemRequest.builder()
                .id(5L)
                .description("Описание")
                .requestor(user)
                .created(LocalDateTime.of(2023, 4, 10, 0, 0, 0, 0))
                .items(new ArrayList<>()).build();


        requestDto = ItemRequestDto.builder()
                .id(5L)
                .description("Описание")
                .requestorId(2L)
                .created(LocalDateTime.of(2000, 3, 10, 0, 0, 0, 0))
                .items(new ArrayList<>()).build();
    }

    @Test
    void findById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(anyLong())).thenReturn(Optional.of(itemRequest));

        ItemRequestDto newItemRequestDto = service.findById(user.getId(), itemRequest.getId());

        assertEquals(newItemRequestDto.getId(), itemRequest.getId());
    }

    @Test
    void findByIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.findById(itemRequest.getId(), user.getId()));
        verify(repository, never()).save(toItemRequest(requestDto, user));
    }

    @Test
    void getAllItemRequests() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findByIdNot(anyLong(), any())).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> newItemRequestDtoList = service.getAllItemRequests(user.getId(), Pageable.unpaged());

        assertEquals(newItemRequestDtoList.size(), 1);
        assertEquals(newItemRequestDtoList.get(0).getId(), itemRequest.getId());
    }

    @Test
    void getAllItemRequestsNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getAllItemRequests(itemRequest.getId(), Pageable.unpaged()));
        verify(repository, never()).save(toItemRequest(requestDto, user));
    }

    @Test
    void getItemRequest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findByRequestorIdOrderByIdDesc(user.getId())).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> itemListRequest = service.getItemRequest(user.getId());

        assertEquals(itemListRequest.size(), 1);
        assertEquals(itemListRequest.get(0).getId(), itemRequest.getId());
    }

    @Test
    void getItemRequestUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getItemRequest(itemRequest.getId()));
        verify(repository, never()).save(toItemRequest(requestDto, user));
    }

    @Test
    void getItemRequestNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ItemRequestNotFoundException.class, () -> service.findById(itemRequest.getId(), user.getId()));
    }

    @Test
    void addItemRequest() {
        assertNotNull(requestDto.getCreated());
        assertEquals(requestDto.getId(), itemRequest.getId());
    }

    @Test
    void addItemRequestNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.addItemRequest(requestDto, user.getId()));
        verify(repository, never()).save(toItemRequest(requestDto, user));
    }
}