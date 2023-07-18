package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplIntegrationTest {

    private final EntityManager em;
    private final UserRepository repository;
    private UserService service;

    private User user;
    private UserDto update;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(repository);

        user = User.builder()
                .name("John")
                .email("john.doe@mail.com").build();

        update = UserDto.builder()
                .id(1L)
                .email("Jon")
                .name("Jon.doe@mail.com").build();
    }

    @Test
    void IntegrationUserUpdate() {
        em.persist(user);
        em.flush();

        UserDto users = service.updateUser(update, user.getId());

        assertEquals(users.getName(), update.getName());
        assertEquals(users.getEmail(), user.getEmail());
    }
}
