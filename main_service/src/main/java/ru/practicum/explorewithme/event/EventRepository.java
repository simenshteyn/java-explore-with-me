package ru.practicum.explorewithme.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByPaidOrderByIdDesc(boolean paid, Pageable pageable);
    List<Event> findByInitiatorIdIn(List<Long> ids, Pageable pageable);
}
