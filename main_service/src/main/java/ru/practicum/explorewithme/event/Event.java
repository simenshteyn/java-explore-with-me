package ru.practicum.explorewithme.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Formula;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Column(length = 2000)
    private String annotation;
    @ManyToOne(targetEntity = Category.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(length = 7000)
    private String description;
    @Column
    private LocalDateTime eventDate;
    @Embedded
    private Location location;
    @Column
    private EventState state;
    @Column
    private Boolean paid;
    @Column
    private Integer participantLimit;
    @Column
    private Boolean requestModeration;
    @Column(length = 120)
    private String title;
    @Column
    private int views;
    @Column
    private LocalDateTime createdOn;
    @Column
    private LocalDateTime publishedOn;
    @ManyToMany(mappedBy = "events")
    @JsonIgnore
    private List<Compilation> compilations;
    @Formula("(SELECT COUNT(*) FROM participations p WHERE p.event_id = event_id AND p.status = 'CONFIRMED')")
    private long confirmedRequests;
}
