package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    final Map<Long, User> addUser = new HashMap<>();

    @Override
    public List<UserDto> findAll() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto findById(long id) {
        return toUserDto(repository.findById(id).orElseThrow(() ->
                new UserNotFoundException("UserServiceImpl: User finById Not Found 404")));
    }

    @Transactional
    @Override
    public UserDto addUser(UserDto user) {
        if (addUser.values().stream()
                .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            throw new UserExistsException("Пользователь уже существует!");
        }
        return toUserDto(repository.save(toUser(user)));
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto user, long id) {
        user.setId(id);
        User users = repository.findById(id).orElseThrow();
        users.setId(id);
        if (user.getName() != null) {
            users.setName(user.getName());
        }
        if (user.getEmail() != null) {
            users.setEmail(user.getEmail());
        }
        return toUserDto(repository.save(users));
    }

    @Transactional
    @Override
    public void deleteUser(long id) {
        repository.deleteById(id);
    }
}
