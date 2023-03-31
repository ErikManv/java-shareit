package ru.practicum.shareit.item.dto;

import org.mapstruct.*;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "authorName", source = "author.name")
    CommentDto toDto(Comment comment);
}
