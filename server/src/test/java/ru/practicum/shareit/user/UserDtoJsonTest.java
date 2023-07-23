package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> jsonUser;

    private UserDto user;

    @BeforeEach
    void setUp() {
        user = new UserDto(
                1L,
                "John",
                "John.doe@mail.com"
        );
    }

    @Test
    void serializationUserTest() throws Exception {
        JsonContent<UserDto> result = jsonUser.write(user);

        assertThat(result).hasJsonPathValue("$.id", user.getId());
        assertThat(result).hasJsonPathValue("$.name", user.getName());
        assertThat(result).hasJsonPathValue("$.email", user.getEmail());
    }

    @Test
    void deserializationUserTest() throws Exception {
        String json = "{\"id\": 1," +
                " \"name\": \"John\"," +
                " \"email\": \"John.doe@mail.com\"}";


        UserDto result = jsonUser.parse(json).getObject();

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }
}