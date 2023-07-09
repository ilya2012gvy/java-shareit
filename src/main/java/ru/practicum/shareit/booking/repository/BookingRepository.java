package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select * from bookings as b " +
            "where b.item_id = ?1 and " +
            "b.start_date < ?2 and " +
            "b.status = 'APPROVED'" +
            "order by b.start_date desc " +
            "limit 1", nativeQuery = true)
    Optional<Booking> getItemLastBooking(long itemId, LocalDateTime localDateTime);

    @Query(value = "select * from bookings as b " +
            "where b.item_id = ?1 and " +
            "b.start_date > ?2 and " +
            "b.status = 'APPROVED'" +
            "order by b.start_date asc " +
            "limit 1", nativeQuery = true)
    Optional<Booking> getItemNextBooking(long itemId, LocalDateTime localDateTime);

    @Query(value = "select * from bookings as b " +
            "where b.booker_id = ?1 and " +
            "b.status = 'APPROVED' and " +
            "b.item_id = ?2 and " +
            "b.end_date < ?3 " +
            "limit 1 ", nativeQuery = true)
    Optional<Booking> getByBookerAndItemBooking(Long bookerId, Long itemId, LocalDateTime localDateTime);


    List<Booking> findAllByItemOwnerIdOrderByStartDesc(long ownerId);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(long ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime start);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus status);

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime start);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);
}
