package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.booking.status.BookingStatus.APPROVED;

@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> jsonItem;

    @Autowired
    private JacksonTester<CommentDto> jsonComment;

    private ItemDto itemDto;
    private CommentDto comment;


    @BeforeEach
    void setUp() {
        BookingRequestDto booking = BookingRequestDto.builder()
                .id(1L)
                .end(LocalDateTime.of(2023, 1, 10, 0, 0, 0, 0))
                .start(LocalDateTime.of(2023, 2, 10, 0, 0, 0, 0))
                .itemId(1L)
                .bookerId(1L)
                .status(APPROVED).build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("John.doe@mail.com")
                .name("John").build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Аккумуляторная")
                .owner(userDto)
                .available(true)
                .requestId(5L)
                .lastBooking(booking)
                .nextBooking(booking)
                .comments(new ArrayList<>()).build();

        comment = new CommentDto(
                1L,
                "text",
                itemDto,
                "authorName",
                LocalDateTime.now());
    }

    @Test
    void testSerializationItem() throws Exception {
        JsonContent<ItemDto> result = jsonItem.write(itemDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.requestId");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Аккумуляторная");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathValue("$.requestId").isEqualTo(5);
    }

    @Test
    void testSerializeComment() throws Exception {
        JsonContent<CommentDto> result = jsonComment.write(comment);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.text");
        assertThat(result).hasJsonPath("$.authorName");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(comment.getAuthorName());
        assertThat(result).hasJsonPathValue("$.created");
    }
}