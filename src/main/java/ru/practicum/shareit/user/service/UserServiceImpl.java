package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> findAll() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) {
        return toUserDto(repository.findById(id));
    }

    @Override
    public UserDto addUser(UserDto user) {
        return toUserDto(repository.addUser(toUser(user)));
    }

    @Override
    public UserDto updateUser(UserDto user, long id) {
        return toUserDto(repository.updateUser(toUser(user), id));
    }

    @Override
    public boolean deleteUser(long id) {
        return repository.deleteUser(id);
    }
}
