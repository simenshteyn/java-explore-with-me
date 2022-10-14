package ru.practicum.explorewithme.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDto {
    @NotNull(message = "Id can't be null")
    private Long id;
    @NotBlank(message = "Name can't be blank")
    @NotNull(message = "Name can't be null")
    private String name;
}
