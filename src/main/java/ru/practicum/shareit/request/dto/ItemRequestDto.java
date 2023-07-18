package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "Отсутствует описание")
    @NotNull
    private String description;
    private Long requestorId;
    private LocalDateTime created;
    private List<ItemDto> items;
}