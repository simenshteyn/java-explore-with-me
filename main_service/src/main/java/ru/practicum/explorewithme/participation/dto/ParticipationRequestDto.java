package ru.practicum.explorewithme.participation.dto;

import lombok.*;
import ru.practicum.explorewithme.participation.ParticipationRequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private ParticipationRequestStatus status;
}
