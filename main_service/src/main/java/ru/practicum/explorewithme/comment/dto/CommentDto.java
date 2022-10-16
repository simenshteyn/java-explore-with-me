package ru.practicum.explorewithme.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long authorId;
    private Long eventId;
    private String text;
    private LocalDateTime createdOn;
}
