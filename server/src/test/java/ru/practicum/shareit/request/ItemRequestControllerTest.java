package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.request.controller.ItemRequestController;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.service.ItemRequestService;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService service;
    private final ObjectMapper mapper;
    private final MockMvc mvc;

    private ItemRequestDto itemRequestDto;
    private ItemRequestDto itemRequest;
    private UserDto userDto;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Аккумуляторная")
                .owner(userDto)
                .available(true)
                .requestId(5L).build();

        userDto = UserDto.builder()
                .id(1L)
                .name("John")
                .email("John.doe@mail.com").build();

        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .items(List.of(itemDto))
                .created(LocalDateTime.now()).build();

        itemRequest = ItemRequestDto.builder()
                .id(1L)
                .description("description").build();


    }

    @Test
    void findById() throws Exception {
        when(service.findById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.items[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.items[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.items[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.items[0].available", is(itemDto.getAvailable())));
    }

    @Test
    void getAllItemRequests() throws Exception {
        when(service.getAllItemRequests(anyLong(), any()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].items[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].items[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].items[0].available", is(itemDto.getAvailable())));
    }

    @Test
    void getItemRequest() throws Exception {
        when(service.getItemRequest(userDto.getId()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].items[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].items[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].items[0].available", is(itemDto.getAvailable())));
    }

    @Test
    void addItemRequest() throws Exception {
        when(service.addItemRequest(any(), anyLong()))
                .thenReturn(itemRequest);

        String result = mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemRequest), result);
        verify(service).addItemRequest(any(), anyLong());
    }
}