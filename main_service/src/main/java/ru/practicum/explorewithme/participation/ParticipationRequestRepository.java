package ru.practicum.explorewithme.participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterIdOrderByIdDesc(Long requesterId);

    List<ParticipationRequest> findAllByEventIdOrderByIdDesc(Long eventId);
}
