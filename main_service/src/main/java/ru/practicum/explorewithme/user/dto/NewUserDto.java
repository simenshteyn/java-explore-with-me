package ru.practicum.explorewithme.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    @Email
    @NotNull(message = "Email can't be null")
    private String email;
    @NotBlank(message = "Name can't be blank")
    @NotNull(message = "Name can't be null")
    @Size(min = 2, max = 255)
    private String name;
}
