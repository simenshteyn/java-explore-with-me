package ru.practicum.explorewithme.event.dto;

import lombok.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventHitDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;

    public static EventHitDto requestToDto(HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EventHitDto result = new EventHitDto();
        result.setIp(request.getRemoteAddr());
        result.setUri(request.getRequestURI());
        result.setApp("myapp");
        result.setTimestamp(LocalDateTime.now().format(formatter));
        return result;
    }
}
