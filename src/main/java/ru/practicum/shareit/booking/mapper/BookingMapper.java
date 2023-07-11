package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

public interface BookingMapper {
    static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(toItemDto(booking.getItem(), null, null, null))
                .booker(toUserDto(booking.getBooker()))
                .status(booking.getStatus()).build();
    }

    static Booking toBooking(BookingRequestDto booking, User user, Item item) {
        return Booking.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(item)
                .booker(user)
                .status(booking.getStatus()).build();
    }

    static BookingRequestDto toBookingDtoRequest(Booking booking) {
        if (booking != null) {
            return BookingRequestDto.builder()
                    .id(booking.getId())
                    .start(booking.getStart())
                    .end(booking.getEnd())
                    .itemId(booking.getItem().getId())
                    .bookerId(booking.getBooker().getId())
                    .status(booking.getStatus()).build();
        }
        return null;
    }

    static List<BookingDto> toBookingListDto(List<Booking> booking) {
        return booking.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}