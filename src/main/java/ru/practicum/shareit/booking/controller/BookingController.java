package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @GetMapping("/{bookingId}")
    public BookingDto findById(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long user) {
        log.info("Поиск бронирования по его id: {}", bookingId);
        return service.findById(bookingId, user);
    }

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingRequestDto booking,
                                 @RequestHeader("X-Sharer-User-Id") long id) {
        log.info("Бронирование предмета с  id: {}", id);
        return service.addBooking(booking, id);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto bookingApproved(@RequestParam("approved") Boolean approved, @PathVariable long bookingId,
                                      @RequestHeader("X-Sharer-User-Id") long user) {
        log.info("Статус подтверждён");
        return service.approved(approved, bookingId, user);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestParam(defaultValue = "ALL") String state,
                                             @RequestHeader("X-Sharer-User-Id") long id) {
        return service.getOwnerBookings(state, id);
    }

    @GetMapping
    public List<BookingDto> getBookingBookings(@RequestParam(defaultValue = "ALL") String state,
                                               @RequestHeader("X-Sharer-User-Id") long id) {
        return service.getBookingBookings(state, id);
    }
}