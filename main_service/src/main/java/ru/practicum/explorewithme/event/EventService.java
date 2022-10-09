package ru.practicum.explorewithme.event;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryStorage;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final CategoryStorage categoryStorage;
    private final ModelMapper modelMapper;

    @Autowired
    public EventService(EventStorage eventStorage, UserStorage userStorage, CategoryStorage categoryStorage, ModelMapper modelMapper) {
        this.eventStorage = eventStorage;
        this.userStorage = userStorage;
        this.categoryStorage = categoryStorage;
        this.modelMapper = modelMapper;
    }

    public List<Event> getAllEvents(int from, int size, boolean paid, EventSort sort) {
        System.out.println("SORT: " + sort.toString());
        return eventStorage.getAllEvents(from, size, paid);
    }

    public List<Event> getEventsByUserIds(List<Long> userIds, int from, int size) {
        return eventStorage.getEventsByUserIds(userIds, from, size);
    }

    public Event createEvent(Long userId, NewEventDto newEventDto) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Category category = categoryStorage.getCategoryById(newEventDto.getCategory()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find category")
        );
        Event event = modelMapper.map(newEventDto, Event.class);
        event.setState(EventState.PENDING);
        event.setCategory(category);
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        return eventStorage.addEvent(event).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add event")
        );
    }

    public Event updateEvent(Long eventId, AdminUpdateEventDto adminUpdateEventDto) {
        Event oldEvent = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        Event event = modelMapper.map(adminUpdateEventDto, Event.class);
        return eventStorage.updateEvent(oldEvent.getId(), event).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to update event")
        );
    }

    public Event publishEventById(Long eventId) {
        Event searchEvent = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Event")
        );
        if (searchEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only pending or canceled events can be changed");
        }
        searchEvent.setState(EventState.PUBLISHED);
        searchEvent.setPublishedOn(LocalDateTime.now());
        return eventStorage.updateEvent(eventId, searchEvent).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to publish event")
        );
    }

    public Event rejectEventById(Long eventId) {
        Event searchEvent = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Event")
        );
        if (searchEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only pending or canceled events can be changed");
        }
        searchEvent.setState(EventState.CANCELED);
        return eventStorage.updateEvent(eventId, searchEvent).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to publish event")
        );
    }
}
