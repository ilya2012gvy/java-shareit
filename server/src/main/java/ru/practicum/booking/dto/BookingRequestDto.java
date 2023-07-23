package ru.practicum.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.booking.status.BookingStatus;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingRequestDto {
    private Long id;
    @NotNull
    @FutureOrPresent()
    private LocalDateTime start;
    @NotNull
    @FutureOrPresent()
    private LocalDateTime end;
    @NotNull
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;
}