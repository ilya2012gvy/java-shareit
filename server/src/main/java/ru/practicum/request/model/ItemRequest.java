package ru.practicum.request.model;

import lombok.*;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "requests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "requestor_id")
    private User requestor;
    private LocalDateTime created;
    @OneToMany(mappedBy = "request")
    private List<Item> items;
}
