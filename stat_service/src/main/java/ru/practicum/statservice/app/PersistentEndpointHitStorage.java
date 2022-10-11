package ru.practicum.statservice.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.statservice.app.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PersistentEndpointHitStorage implements EndpointHitStorage {
    private final EndpointHitRepository endpointHitRepository;

    @Autowired
    public PersistentEndpointHitStorage(EndpointHitRepository endpointHitRepository) {
        this.endpointHitRepository = endpointHitRepository;
    }

    @Override
    public Optional<EndpointHit> addEndpointHit(EndpointHit endpointHit) {
        try {
            return Optional.of(endpointHitRepository.save(endpointHit));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<ViewStatsDto> getViewsStats(String uri, LocalDateTime start, LocalDateTime end) {
        return endpointHitRepository.getViewsStats(uri, start, end);
    }
}
