package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

public interface BookingService {

    BookingDto findById(long bookingId, long user);

    BookingDto addBooking(BookingDtoRequest booking, long id);

    BookingDto approved(Boolean approved, long id, long user);

    List<BookingDto> getOwnerBookings(String state, long id);

    List<BookingDto> getBookingBookings(String state, long id);
}
