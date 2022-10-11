package ru.practicum.statservice.app;

import ru.practicum.statservice.app.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EndpointHitStorage {
    Optional<EndpointHit> addEndpointHit(EndpointHit endpointHit);
    List<ViewStatsDto> getViewsStats(String uri, LocalDateTime start, LocalDateTime end);
}
