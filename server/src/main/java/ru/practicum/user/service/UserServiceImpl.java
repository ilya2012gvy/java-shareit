package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    final Map<Long, User> addUser = new HashMap<>();

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(long id) {
        return UserMapper.toUserDto(repository.findById(id).orElseThrow(() ->
                new UserNotFoundException("UserServiceImpl: User finById Not Found 404")));
    }

    @Transactional
    @Override
    public UserDto addUser(UserDto user) {
        if (addUser.values().stream()
                .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            throw new UserNotFoundException("Пользователь уже существует!");
        }
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(user)));
    }

    @Override
    @Transactional
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
        return UserMapper.toUserDto(repository.save(users));
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        repository.deleteById(id);
    }
}
