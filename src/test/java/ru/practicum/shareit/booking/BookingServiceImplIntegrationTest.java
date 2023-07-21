package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.booking.status.BookingStatus.WAITING;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplIntegrationTest {

    private final BookingService service;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingRepository repository;
    private UserDto saveBooker;
    private ItemDto saveItem;

    @BeforeEach
    void setUp() {
        UserDto owner = UserDto.builder()
                .id(1L)
                .email("John.doe@mail.com")
                .name("John").build();
        UserDto saveOwner = userService.addUser(owner);

        UserDto user = UserDto.builder()
                .id(2L)
                .email("Jon.doe@mail.com")
                .name("Jon").build();
        saveBooker = userService.addUser(user);

        ItemDto item = ItemDto.builder()
                .name("Отвертка малеНькая")
                .available(true)
                .description("Супер откручивалка")
                .owner(saveOwner)
                .build();
        saveItem = itemService.addItem(item, saveOwner.getId());
    }

    @Test
    void addBooking() {
        BookingRequestDto newBooking = BookingRequestDto.builder()
                .itemId(saveItem.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .bookerId(saveBooker.getId())
                .build();
        BookingDto savedBooking = service.addBooking(newBooking, saveBooker.getId());

        Optional<Booking> bookingInRepository = repository.findById(savedBooking.getId());

        assertEquals(bookingInRepository.get().getItem().getId(), newBooking.getItemId());
        assertEquals(bookingInRepository.get().getBooker().getId(), newBooking.getBookerId());
        assertEquals(bookingInRepository.get().getEnd(), newBooking.getEnd());
        assertEquals(bookingInRepository.get().getStart(), newBooking.getStart());
        assertEquals(bookingInRepository.get().getId(), savedBooking.getId());
        assertEquals(bookingInRepository.get().getStatus(), WAITING);
    }
}