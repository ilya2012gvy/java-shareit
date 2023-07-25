package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.item.controller.ItemController;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.service.ItemService;
import ru.practicum.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.booking.status.BookingStatus.APPROVED;

@WebMvcTest(controllers = ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    @MockBean
    private ItemService service;
    private final ObjectMapper mapper;
    private final MockMvc mvc;
    private ItemDto itemDto;
    private UserDto userDto;
    private CommentDto comment;

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

        comment = CommentDto.builder()
                .id(1L)
                .text("comment")
                .item(itemDto)
                .authorName("authorName")
                .created(LocalDateTime.of(2023, 6, 18, 10, 0, 0)).build();
    }

    private ItemDto getItemDto() {
        BookingRequestDto lastBookingDto = BookingRequestDto.builder()
                .id(2L)
                .start(LocalDateTime.of(2023, 6, 16, 10, 0, 0))
                .end(LocalDateTime.of(2023, 6, 18, 10, 0, 0))
                .itemId(1L)
                .bookerId(2L)
                .status(APPROVED).build();

        BookingRequestDto nextBookingDto = BookingRequestDto.builder()
                .id(2L)
                .start(LocalDateTime.of(2023, 6, 19, 10, 0, 0))
                .end(LocalDateTime.of(2023, 6, 20, 10, 0, 0))
                .itemId(1L)
                .bookerId(3L)
                .status(APPROVED).build();

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("comment")
                .item(itemDto)
                .authorName("authorName")
                .created(LocalDateTime.of(2023, 6, 18, 10, 0, 0)).build();

        return ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Аккумуляторная")
                .available(true)
                .owner(userDto)
                .lastBooking(lastBookingDto)
                .nextBooking(nextBookingDto)
                .comments(List.of(commentDto)).build();
    }

    @Test
    void findByIdItem() throws Exception {
        ItemDto dto = getItemDto();
        when(service.findById(anyLong(), anyLong()))
                .thenReturn(dto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.available", is(dto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(dto.getRequestId())))
                .andExpect(jsonPath("$.owner.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.owner.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.owner.name", is(userDto.getName())))
                .andExpect(jsonPath("$.lastBooking.id", is(dto.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(dto.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$.comments[0].id", is(dto.getComments().get(0).getId()), Long.class));
    }

    @Test
    void getItems() throws Exception {
        ItemDto dto = getItemDto();
        when(service.getItems(anyLong(), any()))
                .thenReturn(List.of(dto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(dto.getName())))
                .andExpect(jsonPath("$[0].description", is(dto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(dto.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(dto.getRequestId())))
                .andExpect(jsonPath("$[0].owner.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].owner.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$[0].owner.name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(dto.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.id", is(dto.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].comments[0].id", is(dto.getComments().get(0).getId()), Long.class));
    }

    @Test
    void searchByText() throws Exception {
        when(service.searchByText(anyString(), anyLong(), any()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param("text", "дРелЬ")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    void searchByTextEmpty() {
        assertThat(service.searchByText("", 1L, Pageable.unpaged())).isEmpty();
    }

    @Test
    void addItem() throws Exception {
        when(service.addItem(itemDto, 1L))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    void addItemNotBlankName() throws Exception {
        itemDto.setName(" ");

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addItemNotNullAvailable() throws Exception {
        itemDto.setAvailable(null);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem() throws Exception {
        ItemDto update = new ItemDto();
        update.setId(1L);
        update.setAvailable(false);

        ItemDto newItem = itemDto;
        newItem.setAvailable(false);

        when(service.updateItem(any(), anyLong(), anyLong()))
                .thenReturn(newItem);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .content(mapper.writeValueAsString(update))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newItem.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(newItem.getName())))
                .andExpect(jsonPath("$.description", is(newItem.getDescription())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner())))
                .andExpect(jsonPath("$.available", is(newItem.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(newItem.getRequestId()), Long.class));
    }

    @Test
    void deleteItem() throws Exception {
        mvc.perform(delete("/items/1"))
                .andExpect(status().isOk());

        verify(service).deleteItem(1L);
    }

    @Test
    void addComment() throws Exception {
        when(service.addComment(comment, itemDto.getId(), 1L)).thenReturn(comment);

        String result = mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(mapper.writeValueAsString(comment), result);
        verify(service).addComment(comment, itemDto.getId(), 1L);
    }
}