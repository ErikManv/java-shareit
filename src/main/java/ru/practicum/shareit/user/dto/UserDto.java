package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.markers.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull(groups = {Update.class})
    private Integer id;
    private String name;
    @NotBlank
    @Email
    private String email;
}