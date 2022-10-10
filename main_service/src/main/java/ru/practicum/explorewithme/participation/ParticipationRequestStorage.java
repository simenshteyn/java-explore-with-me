package ru.practicum.explorewithme.participation;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestStorage {
    Optional<ParticipationRequest> addParticipationRequest(ParticipationRequest request);
    Optional<ParticipationRequest> updateParticipationRequest(Long participationId, ParticipationRequest request);
    List<ParticipationRequest> getParticipationRequestsByUserId(Long userId);
    List<ParticipationRequest> getParticipationRequestByEventId(Long eventId);
    Optional<ParticipationRequest> getParticipationRequestById(Long participationRequestId);
    Optional<ParticipationRequest> removeParticipationRequestById(Long participationRequestId);
    List<ParticipationRequest> getRequestsForEvent(Long eventId);
}
