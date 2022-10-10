package ru.practicum.explorewithme.participation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class PersistentParticipationRequestStorage implements ParticipationRequestStorage {
    private final ParticipationRequestRepository participationRequestRepository;

    @Autowired
    public PersistentParticipationRequestStorage(ParticipationRequestRepository participationRequestRepository) {
        this.participationRequestRepository = participationRequestRepository;
    }

    @Override
    public Optional<ParticipationRequest> addParticipationRequest(ParticipationRequest request) {
        try {
            return Optional.of(participationRequestRepository.save(request));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<ParticipationRequest> updateParticipationRequest(Long participationId, ParticipationRequest request) {
        ParticipationRequest searchRequest = participationRequestRepository.findById(participationId).orElse(null);
        if (searchRequest == null) return Optional.empty();
        if (request.getStatus() != null) searchRequest.setStatus(request.getStatus());
        return Optional.of(searchRequest);
    }

    @Override
    public List<ParticipationRequest> getParticipationRequestsByUserId(Long userId) {
        return participationRequestRepository.findAllByRequesterIdOrderByIdDesc(userId);
    }

    @Override
    public List<ParticipationRequest> getParticipationRequestByEventId(Long eventId) {
        return participationRequestRepository.findAllByEventIdOrderByIdDesc(eventId);
    }

    @Override
    public Optional<ParticipationRequest> getParticipationRequestById(Long participationRequestId) {
        return participationRequestRepository.findById(participationRequestId);
    }

    @Override
    public Optional<ParticipationRequest> removeParticipationRequestById(Long participationRequestId) {
        Optional<ParticipationRequest> request = participationRequestRepository.findById(participationRequestId);
        request.ifPresent(r -> participationRequestRepository.deleteById(r.getId()));
        return request;
    }

    @Override
    public List<ParticipationRequest> getRequestsForEvent(Long eventId) {
        return participationRequestRepository.findAllByEventIdOrderByIdDesc(eventId);
    }
}
