package ru.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
}
