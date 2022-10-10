package ru.practicum.explorewithme.participation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.EventStorage;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ParticipationRequestService {
    private final ParticipationRequestStorage participationRequestStorage;
    private final UserStorage userStorage;
    private final EventStorage eventStorage;

    @Autowired
    public ParticipationRequestService(ParticipationRequestStorage participationRequestStorage, UserStorage userStorage, ModelMapper modelMapper, EventStorage eventStorage) {
        this.participationRequestStorage = participationRequestStorage;
        this.userStorage = userStorage;
        this.eventStorage = eventStorage;
    }

    public List<ParticipationRequest> getParticipationRequestsByUserId(Long userId) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        return participationRequestStorage.getParticipationRequestsByUserId(user.getId());
    }

    public ParticipationRequest addParticipationRequestFromUser(Long userId, Long eventId) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only PUBLISHED events are allowed to request");
        }
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't request your own event");
        }
        Integer participationLimit = event.getParticipantLimit();
        if (participationLimit > 0 && participationLimit < countParticipationRequestsForEvent(eventId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't exceed participation limit");
        }
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setCreated(LocalDateTime.now());
        participationRequest.setRequester(user);
        participationRequest.setEvent(event);
        participationRequest.setStatus(ParticipationRequestStatus.PENDING);
        if (!event.getRequestModeration()) participationRequest.setStatus(ParticipationRequestStatus.CONFIRMED);
        return participationRequestStorage.addParticipationRequest(participationRequest).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add participation request")
        );
    }

    public ParticipationRequest cancelParticipationRequestFromUser(Long userId, Long participationId) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        ParticipationRequest participationRequest = participationRequestStorage.getParticipationRequestById(participationId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find participation request")
                );
        participationRequest.setStatus(ParticipationRequestStatus.CANCELED);
        return participationRequestStorage.updateParticipationRequest(participationId, participationRequest).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to remove participation request")
        );
    }

    public List<ParticipationRequest> getUserRequestsForEvent(Long userId, Long eventId) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        if (event.getInitiator().getId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only initiator of event can get all requests");
        }
        return participationRequestStorage.getRequestsForEvent(eventId);
    }

    public ParticipationRequest confirmRequestToEvent(Long userId, Long eventId, Long requestId) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        if (event.getInitiator().getId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You should own event to confirm request");
        }
        if (event.getParticipantLimit() == 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You shouldn't confirm requests for events without limit");
        }
        if (!event.getRequestModeration()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This event don't need moderation for request");
        }
        Long currentRequestsCount = countParticipationRequestsForEvent(eventId);
        if (event.getParticipantLimit() != 0 && currentRequestsCount >= event.getParticipantLimit()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Participation limit is over");
        }
        ParticipationRequest request = participationRequestStorage.getParticipationRequestById(requestId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participation Request not found")
        );
        request.setStatus(ParticipationRequestStatus.CONFIRMED);
        ParticipationRequest result = participationRequestStorage.updateParticipationRequest(requestId, request).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to confirm request")
        );
        // TODO reject pending
        return result;
    }

    public ParticipationRequest rejectRequestForEvent(Long userId, Long eventId, Long requestId) {
        User user = userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You should own event to confirm request");
        }
        if (event.getParticipantLimit() == 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You shouldn't reject requests for events without limit");
        }
        if (!event.getRequestModeration()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This event don't need moderation for request");
        }
        ParticipationRequest request = participationRequestStorage.getParticipationRequestById(requestId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participation Request not found")
        );
        request.setStatus(ParticipationRequestStatus.REJECTED);
        ParticipationRequest result = participationRequestStorage.updateParticipationRequest(requestId, request).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to reject request")
        );
        return result;
    }

    private Long countParticipationRequestsForEvent(Long eventId) {
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
        );
        return (long) participationRequestStorage.getParticipationRequestByEventId(event.getId()).size();

    }
}
