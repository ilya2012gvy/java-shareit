package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(message = "Отсутствует название")
    @NotEmpty
    @NotNull
    private String name;
    @NotBlank(message = "Отсутствует описание")
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private ItemRequestDto request;
    private UserDto owner;
    private BookingDtoRequest lastBooking;
    private BookingDtoRequest nextBooking;
    private List<CommentDto> comments;
}