package ru.practicum.user.mapper;


import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

public interface UserMapper {
    static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail()).build();
    }

    static User toUser(UserDto user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}