package ru.practicum.statservice.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class EndpointHitDto {
    @NotNull(message = "App can't be null")
    @NotBlank(message = "App can't be blank")
    @Size(min = 2, max = 255)
    private String app;
    @NotNull(message = "Url can't be null")
    @NotBlank(message = "Url can't be blank")
    @Size(min = 2, max = 255)
    private String uri;
    @NotNull(message = "Url can't be null")
    @NotBlank(message = "Url can't be blank")
    @Size(min = 2, max = 255)
    private String ip;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
