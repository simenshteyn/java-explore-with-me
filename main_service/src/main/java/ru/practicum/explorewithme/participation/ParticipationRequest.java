package ru.practicum.explorewithme.participation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participations",
        uniqueConstraints={@UniqueConstraint(columnNames = {"event_id", "requester_id"})}
)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ParticipationRequest {
    @Id
    @Column(name = "participation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id")
    private User requester;
//    @Column
    @Enumerated(EnumType.STRING)
    private ParticipationRequestStatus status;
    @Column
    private LocalDateTime created;
}
