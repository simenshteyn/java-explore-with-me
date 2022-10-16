package ru.practicum.explorewithme.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {
    @NotBlank(message = "Text in comment can't be blank")
    @NotNull(message = "Text in comment can't be null")
    @Size(min = 3, max = 1000, message = "Text in comment should be between 3 and 1000 characters")
    private String text;
}
