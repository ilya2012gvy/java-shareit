package ru.practicum.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.repository.BookingRepository;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.exception.ItemRequestNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.CommentRepository;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.repository.ItemRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.booking.mapper.BookingMapper.toBookingDtoRequest;
import static ru.practicum.item.mapper.CommentMapper.*;
import static ru.practicum.item.mapper.ItemMapper.*;
import static ru.practicum.user.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository requestRepository;

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

        ItemDto items = toItemDto(item);
        items.setLastBooking(last);
        items.setNextBooking(next);
        items.setComments(comments);

        return items;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItems(long id, Pageable page) {
        userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("ItemServiceImpl: User getItems Not Found 404"));
        List<Item> items = itemRepository.findByOwnerIdOrderByIdAsc(id, page);
        List<ItemDto> itemDtoRequest = new ArrayList<>();

        for (Item item : items) {
            Long itemId = item.getId();
            List<CommentDto> comments = toListCommentDto(commentRepository.findAllByItemId(itemId));

            BookingRequestDto last = toBookingDtoRequest(bookingRepository
                    .getItemLastBooking(itemId, LocalDateTime.now()).orElse(null));
            BookingRequestDto next = toBookingDtoRequest(bookingRepository
                    .getItemNextBooking(itemId, LocalDateTime.now()).orElse(null));

            ItemDto itemDto = toItemDto(item);
            itemDto.setLastBooking(last);
            itemDto.setNextBooking(next);
            itemDto.setComments(comments);

            itemDtoRequest.add(itemDto);
        }
        return itemDtoRequest;
    }

    @Override
    @Transactional
    public List<ItemDto> searchByText(String text, long user, Pageable page) {
        userRepository.findById(user).orElseThrow(() ->
                new UserNotFoundException("ItemServiceImpl: User searchByText Not Found 404"));
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return toItemListDto(itemRepository.search(text, page));
    }

    @Override
    @Transactional
    public ItemDto addItem(ItemDto item, long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("ItemServiceImpl: User addItem Not Found 404"));
        Long requestId = item.getRequestId();
        item.setOwner(toUserDto(user));
        Item items = toItem(item, user, null);
        if (requestId != null) {
            ItemRequest request = requestRepository.findById(requestId).orElseThrow(()
                    -> new ItemRequestNotFoundException("ItemServiceImpl: Request addItem Not Found 404"));
            items.setRequest(request);
        }
        return toItemDto(itemRepository.save(items));

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
        return toItemDto(itemRepository.save(items));
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