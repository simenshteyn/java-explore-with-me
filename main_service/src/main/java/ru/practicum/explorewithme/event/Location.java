package ru.practicum.explorewithme.event;

import javax.persistence.*;

@Embeddable
public class Location {
    private float lat;
    private float lon;
}
