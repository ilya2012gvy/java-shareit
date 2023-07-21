package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.booking.status.BookingStatus.APPROVED;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private User saveUser;
    private Item saveItem;
    private Booking nextBooking;
    private Booking lastBooking;

    @BeforeEach
    void setUp() {
        User owner = User.builder()
                .id(1L)
                .email("jon.doe@mail.com")
                .name("Jon").build();
        User saveOwner = userRepository.save(owner);

        User user = User.builder()
                .id(2L).email("john.doe@mail.com")
                .name("John").build();
        saveUser = userRepository.save(user);

        Item item = Item.builder()
                .name("Дрель")
                .available(true)
                .description("Аккумуляторная")
                .owner(saveOwner)
                .build();
        saveItem = itemRepository.save(item);

        Booking booking1 = Booking.builder()
                .item(saveItem)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .booker(saveUser)
                .status(APPROVED)
                .id(1L)
                .build();
        nextBooking = bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .item(saveItem)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .booker(saveUser)
                .status(APPROVED)
                .id(2L)
                .build();
        lastBooking = bookingRepository.save(booking2);
    }

    @Test
    void getItemLastBooking() {
        Optional<Booking> booking = bookingRepository.getItemLastBooking(saveItem.getId(), LocalDateTime.now());
        assertEquals(booking.get().getId(), lastBooking.getId());
    }

    @Test
    void getItemNextBooking() {
        Optional<Booking> booking = bookingRepository.getItemNextBooking(saveItem.getId(), LocalDateTime.now());
        assertEquals(booking.get().getId(), nextBooking.getId());
    }

    @Test
    void getByBookerAndItemBooking() {
        Optional<Booking> booking = bookingRepository.getByBookerAndItemBooking(saveUser.getId(), saveItem.getId(), LocalDateTime.now());
        assertEquals(booking.get().getId(), lastBooking.getId());
    }
}
