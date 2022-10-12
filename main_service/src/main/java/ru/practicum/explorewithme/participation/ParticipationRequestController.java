package ru.practicum.explorewithme.participation;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.participation.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
public class ParticipationRequestController {
    private final ParticipationRequestService participationRequestService;
    private final ModelMapper modelMapper;

    @Autowired
    public ParticipationRequestController(ParticipationRequestService participationRequestService, ModelMapper modelMapper) {
        this.participationRequestService = participationRequestService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<?> getParticipationRequestsByUserId(@PathVariable @Positive Long userId) {
        return ResponseEntity.ok(participationRequestService.getParticipationRequestsByUserId(userId).stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<?> addParticipationRequestFromUser(
            @PathVariable @Positive Long userId,
            @RequestParam(required = true) Long eventId) {
        return ResponseEntity.ok(
                convertToDto(participationRequestService.addParticipationRequestFromUser(userId, eventId))
        );
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<?> cancelParticipationRequestFromUser(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long requestId) {
        return ResponseEntity.ok(
                convertToDto(participationRequestService.cancelParticipationRequestFromUser(userId, requestId))
        );
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<?> getUserRequestsForEvent(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        return ResponseEntity.ok(participationRequestService.getUserRequestsForEvent(userId, eventId).stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{requestId}/confirm")
    public ResponseEntity<?> confirmRequestToEvent(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @PathVariable @Positive Long requestId
    ) {
        return ResponseEntity.ok(convertToDto(participationRequestService.confirmRequestToEvent(userId, eventId, requestId)));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{requestId}/reject")
    public ResponseEntity<?> rejectRequestToEvent(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @PathVariable @Positive Long requestId
    ) {
        return ResponseEntity.ok(convertToDto(participationRequestService.rejectRequestForEvent(userId, eventId, requestId)));
    }

    private ParticipationRequestDto convertToDto(ParticipationRequest request) {
        return modelMapper.map(request, ParticipationRequestDto.class);
    }
}
