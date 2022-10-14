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
import ru.practicum.explorewithme.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    public List<Event> getAllEvents(int from, int size, List<Long> categories, boolean paid, EventSort sort) {
        return eventStorage.getAllEvents(from, size, categories, paid);
    }

    public Event getEventById(Long eventId) {
        return eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
    }

    public Event getCurrentUserEventById(Long userId, Long eventId) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only initiator can get full info about event");
        }
        return eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to get event for current user")
        );
    }

    public Event cancelCurrentUserEventById(Long userId, Long eventId) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only initiator can cancel own event");
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only pending event can be cancelled");
        }
        event.setState(EventState.CANCELED);
        return eventStorage.updateEvent(eventId, event).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to cancel event by current user")
        );

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

    public List<Event> getCurrentUserEvents(Long userId, int from, int size) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        return eventStorage.getEventsByUserId(user.getId(), from, size);
    }

    public Event updateCurrentUserEvent(Long userId, UpdateEventDto updateEventDto) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Event event = eventStorage.getEventById(updateEventDto.getEventId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only initiator can update event");
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't update published event");
        }
        if (updateEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Event date can't be less than 2 hours from now");
        }
        Event updatedEvent = modelMapper.map(updateEventDto, Event.class);
        Category category = categoryStorage.getCategoryById(updateEventDto.getCategory()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find category")
        );
        updatedEvent.setCategory(category);
        Event result = eventStorage.updateEvent(event.getId(), updatedEvent).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Unable to update event")
        );
        if (result.getState().equals(EventState.CANCELED)) result.setState(EventState.PENDING);
        return result;
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
