package ru.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDto {
    @NotNull
    @FutureOrPresent()
    private LocalDateTime start;
    @NotNull
    @FutureOrPresent()
    private LocalDateTime end;
    @NotNull
    private Long itemId;
}
