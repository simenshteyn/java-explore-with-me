package ru.practicum.explorewithme.event;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final EventService eventService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminEventController(EventService eventService, ModelMapper modelMapper) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ResponseEntity<?> getEventsByUsersIds(
            @RequestParam(value = "users") List<Long> userIds,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getEventsByUserIds(userIds, from, size).stream()
                .map(this::convertToFullDto).collect(Collectors.toList()));
    }

    @PutMapping("/{eventId}")
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

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<?> publishEventById(@PathVariable @Positive Long eventId) {
        return ResponseEntity.ok(convertToFullDto(eventService.publishEventById(eventId)));
    }

    @PatchMapping("/{eventId}/reject")
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
