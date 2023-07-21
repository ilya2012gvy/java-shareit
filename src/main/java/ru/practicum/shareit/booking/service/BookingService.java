package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {

    BookingDto findById(long bookingId, long user);

    BookingDto addBooking(BookingRequestDto booking, long id);

    BookingDto approved(Boolean approved, long id, long user);

    List<BookingDto> getBookingBookings(String state, long id, Pageable page);

    List<BookingDto> getOwnerBookings(String state, long id, Pageable page);
}
