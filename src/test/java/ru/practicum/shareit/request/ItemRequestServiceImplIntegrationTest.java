package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplIntegrationTest {

    private final UserService userService;
    private final ItemRequestService service;
    private ItemRequestDto saveRequest;
    private ItemRequestDto saveRequest2;
    private List<ItemRequestDto> requestDto;

    @BeforeEach
    void setUp() {
        UserDto user = UserDto.builder()
                .id(1L)
                .email("john.doe@mail.com")
                .name("John").build();
        UserDto saveUser = userService.addUser(user);

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .requestorId(1L)
                .description("description")
                .created(LocalDateTime.now()).build();
        saveRequest = service.addItemRequest(itemRequestDto, saveUser.getId());

        ItemRequestDto itemRequestDto2 = ItemRequestDto.builder()
                .requestorId(saveUser.getId())
                .created(LocalDateTime.of(2000, 2, 11, 0, 13, 0, 0))
                .description("description")
                .build();
        saveRequest2 = service.addItemRequest(itemRequestDto2, saveUser.getId());

        requestDto = service.getItemRequest(user.getId());
    }

    @Test
    void getItemRequest() {
        assertEquals(2, requestDto.size());
        assertEquals(requestDto.get(0).getDescription(), saveRequest.getDescription());
        assertEquals(requestDto.get(1).getDescription(), saveRequest2.getDescription());
    }
}