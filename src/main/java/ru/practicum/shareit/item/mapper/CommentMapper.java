package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public interface CommentMapper {
    static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated()).build();
    }

    static Comment toComment(CommentDto comment, Item item, User user) {
        return Comment.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(item)
                .author(user)
                .created(comment.getCreated()).build();
    }

    static List<CommentDto> toListCommentDto(List<Comment> comment) {
        return comment.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}