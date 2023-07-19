package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.StateAndStatusException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.status.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.status.BookingStatus.REJECTED;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    BookingRepository repository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    BookingService service;
    private BookingServiceImpl bookingService;
    private User user;
    private Item item;
    private User newUser;
    private Booking booking;
    private BookingRequestDto bookingDto;

    @BeforeEach
    void setUp() {
        service = new BookingServiceImpl(repository, userRepository, itemRepository);
        bookingService = new BookingServiceImpl(repository, userRepository, itemRepository);

        user = User.builder()
                .id(1L)
                .email("Jon.doe@mail.com")
                .name("Jon").build();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(5L)
                .requestor(user)
                .created(LocalDateTime.of(2023, 3, 10, 0, 0, 0, 0))
                .items(new ArrayList<>())
                .description("Описание запроса").build();

        item = Item.builder()
                .id(1L)
                .owner(user)
                .name("Ответртка")
                .available(true)
                .description("Описание")
                .request(itemRequest).build();

        bookingDto = BookingRequestDto.builder()
                .id(1L)
                .end(LocalDateTime.of(2023, 1, 10, 0, 0, 0, 0))
                .start(LocalDateTime.of(2023, 2, 10, 0, 0, 0, 0))
                .itemId(item.getId())
                .bookerId(user.getId())
                .status(APPROVED).build();

        booking = Booking.builder()
                .id(1L)
                .end(LocalDateTime.of(2023, 1, 10, 0, 0, 0, 0))
                .start(LocalDateTime.of(2023, 2, 10, 0, 0, 0, 0))
                .item(item)
                .booker(user)
                .status(APPROVED).build();

        newUser = User.builder()
                .id(99L).build();
    }

    @Test
    void findByIdBookingNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> service.findById(booking.getId(), user.getId()));
    }

    @Test
    void addBookingUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.addBooking(bookingDto, user.getId()));
        verify(repository, never()).save(any());
    }

    @Test
    void addBookingItemNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> service.addBooking(bookingDto, user.getId()));
        verify(repository, never()).save(any());
    }


    @Test
    void approvedUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.approved(true, 1L, user.getId()));
        verify(repository, never()).save(any());
    }

    @Test
    void approvedBookingNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> service.approved(true, 1L, user.getId()));
        verify(repository, never()).save(any());
    }

    @Test
    void approvedItemOwner() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));

        booking.getItem().setOwner(newUser);

        assertThrows(ItemNotFoundException.class, () -> service.approved(true, 1L, 1L));
    }

    @Test
    void approvedStatus() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(repository.save(booking)).thenReturn(booking);

        BookingDto bookingDto = service.approved(false, 1L, 1L);

        assertEquals(bookingDto.getStatus(), REJECTED);
    }

    @Test
    void getOwnerBookings() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getOwnerBookings("state", 1L, Pageable.unpaged()));
        verify(repository, never()).save(any());
    }

    @Test
    void getOwnerBookingsStateException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(StateAndStatusException.class, () -> service.getOwnerBookings("Unknown", 1L, Pageable.unpaged()));
    }

    @Test
    void getBookingBookings() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getBookingBookings("state", 1L, Pageable.unpaged()));
        verify(repository, never()).save(any());
    }

    @Test
    void getBookingBookingsStateException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(StateAndStatusException.class, () -> service.getBookingBookings("Unknown", 1L, Pageable.unpaged()));
    }

    @Test
    void getOwnerBookingsAll() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getOwnerBookings("ALL", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByItemOwnerIdOrderByStartDesc(user.getId(), Pageable.unpaged());
    }

    @Test
    void getOwnerBookingsCURRENT() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getOwnerBookings("CURRENT", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(), any(), any());
    }

    @Test
    void getOwnerBookingsPast() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getOwnerBookings("PAST", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getOwnerBookingsFUTURE() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getOwnerBookings("FUTURE", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getOwnerBookingsWAITING() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getOwnerBookings("WAITING", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getOwnerBookingsREJECTED() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getOwnerBookings("REJECTED", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getBookingBookingsAll() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getBookingBookings("ALL", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByBookerIdOrderByStartDesc(user.getId(), Pageable.unpaged());
    }

    @Test
    void getBookingBookingsCURRENT() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getBookingBookings("CURRENT", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(), any(), any());
    }

    @Test
    void getBookingBookingsPast() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getBookingBookings("PAST", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getBookingBookingsFUTURE() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getBookingBookings("FUTURE", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getBookingBookingsWAITING() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getBookingBookings("WAITING", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getBookingBookingsREJECTED() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.getBookingBookings("REJECTED", user.getId(), Pageable.unpaged());

        verify(repository, atLeast(1))
                .findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void validById() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.validById(booking, 99L));
    }

    @Test
    void validItemNotFound() {
        assertDoesNotThrow(() -> bookingService.validItemNotFound(99L, booking));
    }

    @Test
    void validGetAvailable() {
        assertDoesNotThrow(() -> bookingService.validGetAvailable(item));
    }

    @Test
    void validStartBeforeEnd() {
        assertThrows(StateAndStatusException.class, () -> bookingService.validStartBeforeEnd(booking));
    }

    @Test
    void validStatus() {
        assertThrows(StateAndStatusException.class, () -> bookingService.validStatus(true, booking));
    }

    @Test
    void validApprovedNull() {
        assertDoesNotThrow(() -> bookingService.validApprovedNull(true));
    }

    @Test
    void validItemOwner() {
        assertDoesNotThrow(() -> bookingService.validItemOwner(user.getId(), item));
    }
}