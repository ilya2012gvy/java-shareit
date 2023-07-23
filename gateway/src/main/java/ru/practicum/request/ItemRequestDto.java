package ru.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class ItemRequestDto {
    @NotBlank(message = "Отсутствует описание")
    @NotNull
    private String description;
}