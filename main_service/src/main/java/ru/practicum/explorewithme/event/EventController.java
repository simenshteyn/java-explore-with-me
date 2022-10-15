package ru.practicum.explorewithme.event;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/events")
public class EventController {
    private final EventService eventService;
    private final EventClient eventClient;
    private final ModelMapper modelMapper;

    @Autowired
    public EventController(EventService eventService, EventClient eventClient, ModelMapper modelMapper) {
        this.eventService = eventService;
        this.eventClient = eventClient;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllEvents(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "false") boolean paid,
            @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "") String rangeStart,
            @RequestParam(required = false, defaultValue = "") String rangeEnd,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") EventSort sort,
            @RequestParam(required = false, defaultValue = "") String text,
            @RequestParam(required = false, defaultValue = "") List<Long> categories,
            HttpServletRequest request) {
        eventClient.createHit(EventHitDto.requestToDto(request));
        return ResponseEntity.ok(eventService.getAllEvents(from, size, categories, paid, sort).stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable @Positive Long eventId,HttpServletRequest request) {
        eventClient.createHit(EventHitDto.requestToDto(request));
        return ResponseEntity.ok(convertToFullDto(eventService.getEventById(eventId)));
    }

    private EventShortDto convertToDto(Event event) {
        return modelMapper.map(event, EventShortDto.class);
    }

    private EventFullDto convertToFullDto(Event event) {
        return modelMapper.map(event, EventFullDto.class);
    }
}
