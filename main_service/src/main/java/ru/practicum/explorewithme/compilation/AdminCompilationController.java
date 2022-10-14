package ru.practicum.explorewithme.compilation;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminCompilationController(CompilationService compilationService, ModelMapper modelMapper) {
        this.compilationService = compilationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> createCompilation(
            HttpServletRequest request,
            @RequestBody @Valid NewCompilationDto newCompilationDto,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(convertToDto(compilationService.createCompilation(newCompilationDto)));
    }

    @DeleteMapping("/{compilationId}")
    public ResponseEntity<?> removeCompilationById(@PathVariable @Positive Long compilationId) {
        return ResponseEntity.ok(compilationService.removeCompilationById(compilationId));
    }

    @PatchMapping("/{compilationId}/events/{eventId}")
    public ResponseEntity<?> addEventToCompilation(
            @PathVariable @Positive Long compilationId,
            @PathVariable @Positive Long eventId) {
        return ResponseEntity.ok(convertToDto(compilationService.addEventToCompilation(compilationId, eventId)));
    }

    @DeleteMapping("/{compilationId}/events/{eventId}")
    public ResponseEntity<?> removeEventFromCompilation(
            @PathVariable @Positive Long compilationId,
            @PathVariable @Positive Long eventId) {
        return ResponseEntity.ok(convertToDto(compilationService.removeEventFromCompilation(compilationId, eventId)));
    }

    @PatchMapping("/{compilationId}/pin")
    public ResponseEntity<?> pinCompilationById(@PathVariable @Positive Long compilationId) {
        return ResponseEntity.ok(convertToDto(compilationService.pinCompilationById(compilationId)));
    }

    @DeleteMapping("/{compilationId}/pin")
    public ResponseEntity<?> unpinCompilationById(@PathVariable @Positive Long compilationId) {
        return ResponseEntity.ok(convertToDto(compilationService.unpinCompilationById(compilationId)));
    }

    private CompilationDto convertToDto(Compilation compilation) {
        CompilationDto compilationDto = modelMapper.map(compilation, CompilationDto.class);
        return compilationDto;
    }
}
