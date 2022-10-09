package ru.practicum.explorewithme.compilation.dto;

import lombok.*;
import ru.practicum.explorewithme.event.dto.EventShortDto;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private Set<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
