package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.booking.model.Booking;
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
import ru.practicum.item.service.ItemService;
import ru.practicum.item.service.ItemServiceImpl;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.repository.ItemRequestRepository;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.booking.status.BookingStatus.APPROVED;
import static ru.practicum.item.mapper.CommentMapper.toCommentDto;
import static ru.practicum.item.mapper.ItemMapper.toItem;
import static ru.practicum.request.mapper.ItemRequestMapper.toItemRequestDto;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    ItemRepository repository;
    @Mock
    UserRepository userRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ItemRequestRepository requestRepository;

    private ItemService service;
    private User user;
    private Item item;
    private ItemRequest itemRequest;
    private ItemDto itemDto;
    private Booking booking;
    private Comment comment;

    @BeforeEach
    void setUp() {
        service = new ItemServiceImpl(repository, userRepository, commentRepository, bookingRepository, requestRepository);

        user = User.builder()
                .id(1L)
                .name("Jon")
                .email("Jon.doe@mail.com").build();

        UserDto userDto = UserDto.builder()
                .id(2L)
                .name("John")
                .email("John.doe@mail.com").build();

        item = Item.builder()
                .id(2L)
                .owner(user)
                .name("Дрель")
                .available(true)
                .description("Аккумуляторная")
                .request(itemRequest).build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Аккумуляторная")
                .owner(userDto)
                .available(true)
                .requestId(5L).build();

        itemRequest = ItemRequest.builder()
                .id(5L)
                .requestor(user)
                .created(LocalDateTime.of(2000, 3, 10, 0, 0, 0, 0))
                .items(new ArrayList<>())
                .description("Описание запроса").build();

        comment = Comment.builder()
                .created(LocalDateTime.of(2023, 4, 10, 0, 0, 0, 0))
                .item(item)
                .author(user)
                .text("Текст")
                .id(1L)
                .build();

        booking = Booking.builder()
                .item(item)
                .end(LocalDateTime.of(2023, 1, 10, 0, 0, 0, 0))
                .start(LocalDateTime.of(2023, 2, 10, 0, 0, 0, 0))
                .booker(user)
                .status(APPROVED)
                .id(1L)
                .build();
    }

    @Test
    void findByIdItemNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.findById(itemDto.getId(), user.getId()));
        verify(repository, never()).save(toItem(itemDto, user, null));
    }

    @Test
    void findById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDto itemDto = service.findById(user.getId(), item.getId());

        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getOwner().getId(), item.getOwner().getId());
    }

    @Test
    void getItemsNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getItems(user.getId(), Pageable.unpaged()));
        verify(repository, never()).search(any(), any());
    }

    @Test
    void getItems() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findByOwnerIdOrderByIdAsc(anyLong(), any())).thenReturn(List.of(item));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));
        when(bookingRepository.getItemLastBooking(anyLong(), any())).thenReturn(Optional.of(booking));
        when(bookingRepository.getItemNextBooking(anyLong(), any())).thenReturn(Optional.of(booking));

        List<ItemDto> itemListDto = service.getItems(user.getId(), Pageable.unpaged());

        assertEquals(itemListDto.size(), 1);
        assertEquals(itemListDto.get(0).getId(), item.getId());
    }

    @Test
    void searchByTextNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.searchByText("текст", user.getId(), Pageable.unpaged()));
        verify(repository, never()).search(any(), any());
    }

    @Test
    void addItem() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(repository.save(any())).thenReturn(toItem(itemDto, user, toItemRequestDto(itemRequest)));

        ItemDto actual = service.addItem(itemDto, user.getId());

        assertEquals(itemDto, actual);
        verify(repository).save(any());
    }

    @Test
    void addItemNotFoundRequest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ItemRequestNotFoundException.class, () -> service.addItem(itemDto, user.getId()));
        verify(repository, never()).save(toItem(itemDto, user, null));
    }

    @Test
    void addItemNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.addItem(itemDto, user.getId()));
        verify(repository, never()).save(toItem(itemDto, user, null));
    }

    @Test
    void updateItem() {
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
        assertNotEquals(item.getId(), itemDto.getId());
        assertNotEquals(item.getOwner().getId(), itemDto.getOwner().getId());
    }

    @Test
    void updateItemNullPointer() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(NullPointerException.class, () -> service.updateItem(itemDto, 1L, 1L));
    }

    @Test
    void updateItemNotFoundUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.updateItem(itemDto, user.getId(), itemDto.getId()));
        verify(repository, never()).save(toItem(itemDto, user, null));
    }

    @Test
    void updateItemNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> service.updateItem(itemDto, user.getId(), itemDto.getId()));
        verify(repository, never()).save(toItem(itemDto, user, null));
    }

    @Test
    void deleteItem() {
        service.deleteItem(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void addComment() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.getByBookerAndItemBooking(any(), any(), any())).thenReturn(Optional.of(booking));
        when(commentRepository.save(any())).thenReturn(comment);

        CommentDto commentDto = service.addComment(toCommentDto(comment), user.getId(), item.getId());

        assertEquals(comment.getId(), commentDto.getId());
    }

    @Test
    void addCommentValid() {
        assertThrows(ValidationException.class, () -> service.addComment(new CommentDto(), 1L, 1L));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void addCommentItemValid() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> service.addComment(new CommentDto(), 1L, 1L));
        verify(commentRepository, never()).save(any());
    }
}