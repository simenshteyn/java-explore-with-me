package ru.practicum.statservice.app;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.statservice.app.dto.EndpointHitDto;
import ru.practicum.statservice.app.dto.GetStatsDto;
import ru.practicum.statservice.app.dto.ViewStatsDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EndpointHitService {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final EndpointHitStorage endpointHitStorage;
    private final ModelMapper modelMapper;

    @Autowired
    public EndpointHitService(EndpointHitStorage endpointHitStorage, ModelMapper modelMapper) {
        this.endpointHitStorage = endpointHitStorage;
        this.modelMapper = modelMapper;
    }

    public EndpointHit createHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = modelMapper.map(endpointHitDto, EndpointHit.class);
        return endpointHitStorage.addEndpointHit(endpointHit).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add hit")
        );
    }

    public List<ViewStatsDto> getStats(GetStatsDto getStatsDto) {
        if (getStatsDto.getUris() == null || getStatsDto.getUris().size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find uris");
        }
        LocalDateTime startTime = convertURLtoTime(getStatsDto.getStart());
        LocalDateTime endTime = convertURLtoTime(getStatsDto.getEnd());
        return getStatsDto.getUris().stream()
                .map(uri -> endpointHitStorage.getViewsStats(uri, startTime, endTime))
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    private LocalDateTime convertURLtoTime(String urlString) {
        try {
            String timeString = java.net.URLDecoder.decode(urlString, StandardCharsets.UTF_8);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            return LocalDateTime.parse(timeString, formatter);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to encode date string");
        }
    }
}
