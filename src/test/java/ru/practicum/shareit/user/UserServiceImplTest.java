package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    UserRepository repository;

    private UserService service;

    User user;
    UserDto update;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(repository);

        user = new User(
                1L,
                "John",
                "john.doe@mail.com");

        update = UserDto.builder()
                .id(1L)
                .email("update")
                .name("updateUser@mail.com").build();
    }


    @Test
    void getAllUsers() {
        List<User> userList = List.of(user);

        when(repository.findAll()).thenReturn(userList);

        List<UserDto> resultUserDtoList = service.findAll();
        List<UserDto> userDtoList = List.of(toUserDto(user));

        assertEquals(1, resultUserDtoList.size());
        assertEquals(userDtoList.get(0), resultUserDtoList.get(0));
    }

    @Test
    void findByIdUsers() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        UserDto userDto = toUserDto(user);
        UserDto resultUserDto = service.findById(1L);

        assertEquals(userDto, resultUserDto);
    }

    @Test
    void addUsers() {
        when(repository.save(user)).thenReturn(user);

        UserDto userDto = toUserDto(user);
        UserDto result = service.addUser(userDto);

        assertEquals(userDto, result);
        verify(repository).save(user);
    }

    @Test
    void updateNewUser() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        UserDto actual = service.updateUser(update, update.getId());

        assertEquals(actual, update);
        verify(repository).save(user);
    }

    @Test
    void updateUserNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.findById(update.getId()));
        verify(repository, never()).save(any());
    }

    @Test
    void deleteUser() {
        service.deleteUser(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
