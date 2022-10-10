package ru.practicum.explorewithme.event;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
public class EventController {
    private final EventService eventService;
    private final ModelMapper modelMapper;

    @Autowired
    public EventController(EventService eventService, ModelMapper modelMapper) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/events")
    public ResponseEntity<?> getAllEvents(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "false") boolean paid,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") EventSort sort) {
        return ResponseEntity.ok(eventService.getAllEvents(from, size, paid, sort).stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable @Positive Long eventId) {
        return ResponseEntity.ok(convertToFullDto(eventService.getEventById(eventId)));
    }

    @GetMapping("/admin/events")
    public ResponseEntity<?> getEventsByUsersIds(
            @RequestParam(value = "users") List<Long> userIds,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getEventsByUserIds(userIds, from, size).stream()
                .map(this::convertToFullDto).collect(Collectors.toList()));
    }


    @PostMapping("/users/{userId}/events")
    @Validated
    public ResponseEntity<?> createEvent(
            HttpServletRequest request,
            @RequestBody @Valid NewEventDto newEventDto,
            @PathVariable @Positive Long userId,
            Errors errors){
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(convertToFullDto(eventService.createEvent(userId, newEventDto)));
    }

    @PatchMapping("/users/{userId}/events")
    @Validated
    public ResponseEntity<?> updateCurrentUserEvent(
            HttpServletRequest request,
            @RequestBody @Valid UpdateEventDto updateEventDto,
            @PathVariable @Positive Long userId,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(convertToFullDto(eventService.updateCurrentUserEvent(userId, updateEventDto)));

    }

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<?> getCurrentUserEvents(
            @PathVariable @Positive Long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getCurrentUserEvents(userId, from, size).stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<?> getCurrentUserEventById(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        return ResponseEntity.ok(convertToFullDto(eventService.getCurrentUserEventById(userId, eventId)));
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<?> cancelCurrentUserEventById(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        return ResponseEntity.ok(convertToFullDto(eventService.cancelCurrentUserEventById(userId, eventId)));
    }

    @PutMapping("/admin/events/{eventId}")
    @Validated
    public ResponseEntity<?> updateEvent(
            HttpServletRequest request,
            @RequestBody @Valid AdminUpdateEventDto adminUpdateEventDto,
            @PathVariable @Positive Long eventId,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(convertToFullDto(eventService.updateEvent(eventId, adminUpdateEventDto)));
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public ResponseEntity<?> publishEventById(@PathVariable @Positive Long eventId) {
        return ResponseEntity.ok(convertToFullDto(eventService.publishEventById(eventId)));
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public ResponseEntity<?> rejectEventById(@PathVariable @Positive Long eventId) {
        return ResponseEntity.ok(convertToFullDto(eventService.rejectEventById(eventId)));
    }


    private EventShortDto convertToDto(Event event) {
        return modelMapper.map(event, EventShortDto.class);
    }

    private EventFullDto convertToFullDto(Event event) {
        return modelMapper.map(event, EventFullDto.class);
    }
}
