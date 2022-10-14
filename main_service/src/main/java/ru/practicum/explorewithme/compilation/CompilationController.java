package ru.practicum.explorewithme.compilation;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;

import javax.validation.constraints.Positive;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/compilations")
public class CompilationController {
    private final CompilationService compilationService;
    private final ModelMapper modelMapper;

    @Autowired
    public CompilationController(CompilationService compilationService, ModelMapper modelMapper) {
        this.compilationService = compilationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCompilations(
           @RequestParam(required = false, defaultValue = "0") int from,
           @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(compilationService.getAllCompilations(from, size).getContent().stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{compilationId}")
    public ResponseEntity<?> getCompilationById(@PathVariable @Positive Long compilationId) {
        return ResponseEntity.ok(convertToDto(compilationService.getCompilationById(compilationId)));
    }

    private CompilationDto convertToDto(Compilation compilation) {
        CompilationDto compilationDto = modelMapper.map(compilation, CompilationDto.class);
        return compilationDto;
    }

}
