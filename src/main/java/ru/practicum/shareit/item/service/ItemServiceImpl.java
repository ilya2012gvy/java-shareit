package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDtoRequest;
import static ru.practicum.shareit.item.mapper.CommentMapper.*;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public ItemDto findById(long id, long user) {
        userRepository.findById(user).orElseThrow(() ->
                new UserNotFoundException("ItemServiceImpl: User findById Not Found 404"));
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException("ItemServiceImpl: Item findById Not Found 404"));
        BookingRequestDto last = null;
        BookingRequestDto next = null;
        if (item.getOwner().getId().equals(user)) {
            last = toBookingDtoRequest(bookingRepository
                    .getItemLastBooking(id, LocalDateTime.now()).orElse(null));
            next = toBookingDtoRequest(bookingRepository
                    .getItemNextBooking(id, LocalDateTime.now()).orElse(null));
        }
        List<CommentDto> comments = toListCommentDto(commentRepository.findAllByItemId(id));

        return toItemDto(item, last, next, comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItems(long id) {
        userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("ItemServiceImpl: User findById Not Found 404"));
        List<Item> items = itemRepository.findByOwnerIdOrderByIdAsc(id);
        List<ItemDto> itemDtoRequest = new ArrayList<>();

        for (Item item : items) {
            Long itemId = item.getId();
            List<CommentDto> comments = toListCommentDto(commentRepository.findAllByItemId(itemId));

            BookingRequestDto last = toBookingDtoRequest(bookingRepository
                    .getItemLastBooking(itemId, LocalDateTime.now()).orElse(null));
            BookingRequestDto next = toBookingDtoRequest(bookingRepository
                    .getItemNextBooking(itemId, LocalDateTime.now()).orElse(null));

            itemDtoRequest.add(toItemDto(item, last, next, comments));
        }
        return itemDtoRequest;
    }

    @Override
    @Transactional
    public List<ItemDto> searchByText(String text, long user) {
        userRepository.findById(user).orElseThrow(() ->
                new UserNotFoundException("ItemServiceImpl: User searchByText Not Found 404"));
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return toItemListDto(itemRepository.search(text));
    }

    @Override
    @Transactional
    public ItemDto addItem(ItemDto item, long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("ItemServiceImpl: User addItem Not Found 404"));
        item.setOwner(toUserDto(user));
        return toItemDto(itemRepository.save(toItem(item, user)), null, null, null);
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto item, long id, long user) {
        userRepository.findById(user).orElseThrow(() ->
                new UserNotFoundException("ItemServiceImpl: User updateItem Not Found 404"));
        Item items = itemRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException("ItemServiceImpl: Item updateItem Not Found 404"));

        if (item.getName() != null) {
            items.setName(item.getName());
        }
        if (item.getDescription() != null) {
            items.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            items.setAvailable(item.getAvailable());
        }
        return toItemDto(itemRepository.save(items), null, null, null);
    }

    @Override
    @Transactional
    public void deleteItem(long id) {
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentDto comment, long itemId, long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ValidationException("ItemServiceImpl: Item comment Bad Request 400"));
        User author = userRepository.findById(userId).orElseThrow(() ->
                new ValidationException("ItemServiceImpl: User comment Bad Request 400"));
        bookingRepository.getByBookerAndItemBooking(userId, itemId, LocalDateTime.now()).orElseThrow(() ->
                new ValidationException("ItemServiceImpl: Booking comment Bad Request 400"));

        Comment comments = toComment(comment, item, author);

        comments.setCreated(LocalDateTime.now());

        return toCommentDto(commentRepository.save(comments));
    }
}