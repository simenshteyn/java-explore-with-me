package ru.practicum.statservice.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statservice.app.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.statservice.app.dto.ViewStatsDto(app, uri, count(*) as hits) " +
            "FROM EndpointHit e WHERE e.uri=?1 AND e.timestamp BETWEEN ?2 AND ?3 GROUP BY app, uri")
    List<ViewStatsDto> getViewsStats(String uri, LocalDateTime start, LocalDateTime end);
}
