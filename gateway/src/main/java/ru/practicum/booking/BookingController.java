package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient client;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long user) {
        log.info("Поиск бронирования по его id: {}", bookingId);
        return client.findById(bookingId, user);
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@Valid @RequestBody BookingDto booking,
                                             @RequestHeader("X-Sharer-User-Id") long id) {
        log.info("Бронирование предмета с  id: {}", id);
        return client.addBooking(booking, id);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> bookingApproved(@RequestParam("approved") Boolean approved, @PathVariable long bookingId,
                                                  @RequestHeader("X-Sharer-User-Id") long user) {
        log.info("Статус подтверждён");
        return client.bookingApproved(approved, bookingId, user);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestParam(defaultValue = "ALL") String state,
                                                   @RequestHeader("X-Sharer-User-Id") long id,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        return client.getOwnerBookings(state, id, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingBookings(@RequestParam(defaultValue = "ALL") String state,
                                                     @RequestHeader("X-Sharer-User-Id") long id,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        return client.getBookingBookings(state, id, from, size);
    }
}