package ru.practicum.explorewithme.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;
    @Column(length = 1000)
    private String text;
    @Column
    private LocalDateTime createdOn = LocalDateTime.now();
}
