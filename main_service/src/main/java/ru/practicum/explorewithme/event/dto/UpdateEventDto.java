package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventDto {
    private String annotation;
    private Long category;
    @NotBlank(message = "Description can't be blank")
    @NotNull(message = "Description can't be null")
    @Size(min = 20, max = 7000, message = "Description should be between 20 and 7000 chars")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull(message = "Event Id can't be null")
    private Long eventId;
    private Boolean paid;
    private Integer participantLimit;
    @NotBlank(message = "Title can't be blank")
    @NotNull(message = "Title can't be null")
    @Size(min = 3, max = 120, message = "Title should be between 3 and 120 chars")
    private String title;
}
