package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
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
    private Long requestId;
    private UserDto owner;
    private BookingRequestDto lastBooking;
    private BookingRequestDto nextBooking;
    private List<CommentDto> comments;
}