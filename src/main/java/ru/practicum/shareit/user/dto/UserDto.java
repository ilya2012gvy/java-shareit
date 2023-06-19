package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotEmpty
    @NotBlank(message = "Отсутствует название")
    @NotNull
    private String name;
    @NotEmpty
    @NotBlank(message = "E-mail не может быть пустым")
    @Email(message = "Введен некорректный e-mail")
    private String email;
}
