package ru.practicum.explorewithme.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByPaidOrderByIdDesc(boolean paid, Pageable pageable);

    List<Event> findByCategoryIdInAndPaidOrderByIdDesc(List<Long> ids, boolean paid, Pageable pageable);

    List<Event> findByInitiatorIdIn(List<Long> ids, Pageable pageable);

    List<Event> findAllByInitiatorIdOrderByIdDesc(Long initiatorId, Pageable pageable);
}
