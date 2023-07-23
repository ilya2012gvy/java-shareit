package ru.practicum.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerIdOrderByIdAsc(long id, Pageable pageable);

    @Query("select i " +
            "from Item as i " +
            "where (Lower(i.name) " +
            "like '%'||Lower(?1)||'%' or Lower(i.description) " +
            "like '%'||Lower(?1)||'%') " +
            "and i.available = TRUE")
    List<Item> search(String text, Pageable pageable);
}
