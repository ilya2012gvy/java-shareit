package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private UserDto owner;
    private BookingRequestDto lastBooking;
    private BookingRequestDto nextBooking;
    private List<CommentDto> comments;
}