package ru.practicum.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.status.BookingStatus;

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

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(long ownerId, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(long ownerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus status, Pageable pageable);
}
