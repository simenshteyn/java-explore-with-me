package ru.practicum.explorewithme.participation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterIdOrderByIdDesc(Long requesterId);
    List<ParticipationRequest> findAllByEventIdOrderByIdDesc(Long eventId);
}
