package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.status.BookingStatus.APPROVED;


@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {

    private final ObjectMapper mapper;
    private final MockMvc mvc;
    @MockBean
    private BookingService service;

    private BookingRequestDto bookingRequestDto;
    private BookingDto bookingDto;
    private ItemDto itemDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        bookingRequestDto = BookingRequestDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(1L)
                .bookerId(1L)
                .status(APPROVED)
                .build();

        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(itemDto)
                .booker(userDto)
                .status(APPROVED)
                .build();

        userDto = new UserDto(
                1L,
                "John.doe@mail.com",
                "John");

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Аккумуляторная")
                .owner(userDto)
                .available(true)
                .requestId(5L).build();
    }

    @Test
    void findById() throws Exception {
        when(service.findById(anyLong(), anyLong())).thenReturn(bookingDto);

        String result = mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookingDto), result);
        verify(service).findById(anyLong(), anyLong());
    }

    @Test
    void addBooking() throws Exception {
        when(service.addBooking(any(), anyLong()))
                .thenReturn(bookingDto);

        String result = mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookingDto), result);
        verify(service).addBooking(any(), anyLong());
    }

    @Test
    void approved() throws Exception {
        when(service.approved(anyBoolean(), anyLong(), anyLong()))
                .thenReturn(bookingDto);

        String result = mvc.perform(patch("/bookings/1")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookingDto), result);
        verify(service).approved(anyBoolean(), anyLong(), anyLong());
    }

    @Test
    void getBookings() throws Exception {
        when(service.getBookingBookings(anyString(), anyLong(), any()))
                .thenReturn(List.of(bookingDto));

        String result = mvc.perform(get("/bookings")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(bookingDto)), result);
        verify(service).getBookingBookings(anyString(), anyLong(), any());
    }

    @Test
    void getBookingsOwner() throws Exception {
        when(service.getOwnerBookings(anyString(), anyLong(), any()))
                .thenReturn(List.of(bookingDto));

        String result = mvc.perform(get("/bookings/owner")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(bookingDto)), result);
        verify(service).getOwnerBookings(anyString(), anyLong(), any());
    }
}