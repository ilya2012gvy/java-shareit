package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.StateAndStatusException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.*;
import static ru.practicum.shareit.booking.status.BookingStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto findById(long bookingId, long user) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("BookingServiceImpl: Booking findBookingById Not Found 404"));
        validById(booking, user);
        return toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto addBooking(BookingRequestDto bookingRequestDto, long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("BookingServiceImpl: User addBookings Not Found 404"));
        Item item = itemRepository.findById(bookingRequestDto.getItemId()).orElseThrow(() ->
                new ItemNotFoundException("BookingServiceImpl: Item addBookings Not Found 404"));

        validGetAvailable(item);

        Booking booking = toBooking(bookingRequestDto, user, item);

        booking.setStatus(WAITING);

        validStartBeforeEnd(booking);

        validItemNotFound(id, booking);

        return toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto approved(Boolean approved, long id, long user) {
        userRepository.findById(user).orElseThrow(() ->
                new UserNotFoundException("BookingServiceImpl: User approved Not Found 404"));
        Booking booking = bookingRepository.findById(id).orElseThrow(() ->
                new BookingNotFoundException("BookingServiceImpl: Booking approved Not Found 404"));

        validItemOwner(user, booking.getItem());

        validApprovedNull(approved);

        validStatus(approved, booking);

        if (approved) {
            booking.setStatus(APPROVED);
        } else booking.setStatus(REJECTED);

        return toBookingDto(bookingRepository.save(booking));
    }

    public List<BookingDto> getOwnerBookings(String state, long id) {
        userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("BookingServiceImpl: User getBookingBookings Not Found 404"));
        switch (state) {
            case "ALL":
                return toBookingListDto(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(id));
            case "CURRENT":
                return toBookingListDto(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(id, LocalDateTime.now(), LocalDateTime.now()));
            case "PAST":
                return toBookingListDto(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(id, LocalDateTime.now()));
            case "FUTURE":
                return toBookingListDto(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(id, LocalDateTime.now()));
            case "WAITING":
                return toBookingListDto(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(id, WAITING));
            case "REJECTED":
                return toBookingListDto(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(id, REJECTED));
            default:
                throw new StateAndStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public List<BookingDto> getBookingBookings(String state, long id) {
        userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("BookingServiceImpl: User getBookingBookings Not Found 404"));
        switch (state) {
            case "ALL":
                return toBookingListDto(bookingRepository.findAllByBookerIdOrderByStartDesc(id));
            case "CURRENT":
                return toBookingListDto(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(id, LocalDateTime.now(), LocalDateTime.now()));
            case "PAST":
                return toBookingListDto(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(id, LocalDateTime.now()));
            case "FUTURE":
                return toBookingListDto(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(id, LocalDateTime.now()));
            case "WAITING":
                return toBookingListDto(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(id, WAITING));
            case "REJECTED":
                return toBookingListDto(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(id, REJECTED));
            default:
                throw new StateAndStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private void validById(Booking booking, Long id) {
        if (id.equals(booking.getBooker().getId()) || id.equals(booking.getItem().getOwner().getId())) {
            return;
        }
        throw new BookingNotFoundException("Просмотр бронирования доступен только владельцу или клиенту");
    }

    private void validItemNotFound(Long userId, Booking booking) {
        if (!booking.getItem().getId().equals(userId)) {
            return;
        }
        throw new BookingNotFoundException("Предмет не найден!");

    }

    private void validGetAvailable(Item item) {
        if (item.getAvailable()) {
            return;
        }
        throw new ValidationException("Предмет недоступен для бронирования!");
    }

    private void validStartBeforeEnd(Booking booking) {
        if (booking.getEnd().isAfter(booking.getStart())) {
            return;
        }
        throw new StateAndStatusException("Дата старта должна быть рньше даты окончания!");
    }

    private void validStatus(Boolean approved, Booking booking) {
        if (approved && booking.getStatus().equals(APPROVED)) {
            throw new StateAndStatusException("Статус APPROVED не может быть создан");
        }
    }

    private void validApprovedNull(Boolean approved) {
        if (approved == null) {
            throw new ValidationException("Не задано действие");
        }
    }

    private void validItemOwner(Long userId, Item item) {
        if (item.getOwner().getId().equals(userId)) {
            return;
        }
        throw new ItemNotFoundException("Нет доступа к редокрированию предмета!");

    }
}