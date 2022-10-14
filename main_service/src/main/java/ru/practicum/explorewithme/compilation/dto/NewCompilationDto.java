package ru.practicum.explorewithme.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private List<Integer> events;
    private Boolean pinned;
    @NotBlank(message = "Title can't be blank")
    @NotNull(message = "Title can't be null")
    @Size(min = 2, max = 255)
    private String title;
}
