package ru.practicum.explorewithme.event;

import lombok.*;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private float lat;
    private float lon;
}
