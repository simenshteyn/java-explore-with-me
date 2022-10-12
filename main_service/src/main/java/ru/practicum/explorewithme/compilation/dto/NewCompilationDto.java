package ru.practicum.explorewithme.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private String title;
}
