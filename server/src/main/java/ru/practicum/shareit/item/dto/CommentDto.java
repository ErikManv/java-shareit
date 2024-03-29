package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @NotEmpty
    private String text;

    private String authorName;

    private LocalDateTime created;
}
