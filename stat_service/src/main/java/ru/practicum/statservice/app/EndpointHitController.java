package ru.practicum.statservice.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statservice.app.dto.EndpointHitDto;
import ru.practicum.statservice.app.dto.GetStatsDto;
import ru.practicum.statservice.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @Autowired
    public EndpointHitController(EndpointHitService endpointHitService) {
        this.endpointHitService = endpointHitService;
    }

    @PostMapping("/hit")
    @Validated
    public ResponseEntity<?> createHit(
            HttpServletRequest request,
            @RequestBody @Valid EndpointHitDto hitDto,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(endpointHitService.createHit(hitDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(
        @RequestParam(required = true) String start,
        @RequestParam(required = true) String end,
        @RequestParam(required = false, defaultValue = "null") List<String> uris,
        @RequestParam(required = false, defaultValue = "false") boolean unique) {
        GetStatsDto getStatsDto = new GetStatsDto(start, end, uris, unique);
        return ResponseEntity.ok(endpointHitService.getStats(getStatsDto));
    }
}
