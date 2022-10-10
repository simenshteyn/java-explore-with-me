package ru.practicum.explorewithme.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.validator.OffsetBasedPageRequest;

import java.util.List;
import java.util.Optional;

@Component
public class PersistentEventStorage implements EventStorage {
    private final EventRepository eventRepository;

    @Autowired
    public PersistentEventStorage(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Optional<Event> addEvent(Event event) {
        return Optional.of(eventRepository.save(event));
    }

    @Override
    public Optional<Event> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    public List<Event> getAllEvents(int from, int size, boolean paid) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        return eventRepository.findAllByPaidOrderByIdDesc(paid, pageable);
    }

    @Override
    public List<Event> getEventsByUserIds(List<Long> userIds, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        return eventRepository.findByInitiatorIdIn(userIds, pageable);
    }

    @Override
    public List<Event> getEventsByUserId(Long userId, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        return eventRepository.findAllByInitiatorIdOrderByIdDesc(userId, pageable);
    }

    @Override
    @Transactional
    public Optional<Event> updateEvent(Long eventId, Event event) {
        Event searchEvent = eventRepository.findById(eventId).orElse(null);
        if (searchEvent == null) return Optional.empty();
        if (event.getAnnotation() != null) searchEvent.setAnnotation(event.getAnnotation());
        if (event.getCategory() != null) searchEvent.setCategory(event.getCategory());
        if (event.getDescription() != null) searchEvent.setDescription(event.getDescription());
        if (event.getEventDate() != null) searchEvent.setEventDate(event.getEventDate());
        if (event.getLocation() != null) searchEvent.setLocation(event.getLocation());
        if (event.getPaid() != null) searchEvent.setPaid(event.getPaid());
        if (event.getParticipantLimit() != null) searchEvent.setParticipantLimit(event.getParticipantLimit());
        if (event.getRequestModeration() != null) searchEvent.setRequestModeration(event.getRequestModeration());
        if (event.getTitle() != null) searchEvent.setTitle(event.getTitle());
        return Optional.of(searchEvent);
    }
}
