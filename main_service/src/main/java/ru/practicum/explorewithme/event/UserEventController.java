package ru.practicum.explorewithme.event;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/users")
public class UserEventController {
    private final EventService eventService;
    private final EventClient eventClient;
    private final ModelMapper modelMapper;

    @Autowired
    public UserEventController(EventService eventService, EventClient eventClient, ModelMapper modelMapper) {
        this.eventService = eventService;
        this.eventClient = eventClient;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/{userId}/events")
    @Validated
    public ResponseEntity<?> createEvent(
            HttpServletRequest request,
            @RequestBody @Valid NewEventDto newEventDto,
            @PathVariable @Positive Long userId,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(convertToFullDto(eventService.createEvent(userId, newEventDto)));
    }

    @PatchMapping("/{userId}/events")
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

    @GetMapping("/{userId}/events")
    public ResponseEntity<?> getCurrentUserEvents(
            @PathVariable @Positive Long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getCurrentUserEvents(userId, from, size).stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<?> getCurrentUserEventById(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        return ResponseEntity.ok(convertToFullDto(eventService.getCurrentUserEventById(userId, eventId)));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<?> cancelCurrentUserEventById(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        return ResponseEntity.ok(convertToFullDto(eventService.cancelCurrentUserEventById(userId, eventId)));
    }

    private EventShortDto convertToDto(Event event) {
        return modelMapper.map(event, EventShortDto.class);
    }

    private EventFullDto convertToFullDto(Event event) {
        return modelMapper.map(event, EventFullDto.class);
    }
}
