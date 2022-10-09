package ru.practicum.explorewithme.compilation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompilationService {
    private final CompilationStorage compilationStorage;
    private final EventStorage eventStorage;
    private final ModelMapper modelMapper;

    @Autowired
    public CompilationService(CompilationStorage compilationStorage, EventStorage eventStorage, ModelMapper modelMapper) {
        this.compilationStorage = compilationStorage;
        this.eventStorage = eventStorage;
        this.modelMapper = modelMapper;
    }

    public Page<Compilation> getAllCompilations(int from, int size) {
        return compilationStorage.getAllCompilations(from, size);
    }

    public Compilation getCompilationById(Long compilationId) {
        return compilationStorage.getCompilationById(compilationId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found")
        );
    }


    public Compilation createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = convertToEntity(newCompilationDto);
        List<Event> events = newCompilationDto.getEvents().stream()
                .map(i -> eventStorage.getEventById(Long.valueOf(i)).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find event")
                ))
                .collect(Collectors.toList());
        compilation.setEvents(events);
        return compilationStorage.addCompilation(compilation).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add compilation")
        );
    }

    public Compilation addEventToCompilation(Long compilationId, Long eventId) {
        System.out.println("STARTED METHOD");
        Compilation compilation = compilationStorage.getCompilationById(compilationId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found")
        );
        System.out.println("COMPILATION: " + compilation);
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found")
        );
        System.out.println("EVENT: " + event);
        if (!compilation.getEvents().contains(event)) compilation.getEvents().add(event);
        return compilationStorage.updateCompilation(compilationId, compilation).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add event to compilation")
        );
    }

    public Compilation removeEventFromCompilation(Long compilationId, Long eventId) {
        Compilation compilation = compilationStorage.getCompilationById(compilationId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found")
        );
        Event event = eventStorage.getEventById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found")
        );
        compilation.getEvents().remove(event);
        return compilationStorage.updateCompilation(compilationId, compilation).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to remove event from compilation")
        );

    }

    public Compilation removeCompilationById(Long compilationId) {
        return compilationStorage.deleteCompilationById(compilationId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find compilation")
        );
    }

    public Compilation pinCompilationById(Long compilationId) {
        Compilation compilation = compilationStorage.getCompilationById(compilationId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found")
        );
        compilation.setPinned(true);
        return compilationStorage.updateCompilation(compilationId, compilation).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to pin compilation")
        );
    }

    public Compilation unpinCompilationById(Long compilationId) {
        Compilation compilation = compilationStorage.getCompilationById(compilationId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found")
        );
        compilation.setPinned(false);
        return compilationStorage.updateCompilation(compilationId, compilation).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to unpin compilation")
        );
    }

    private Compilation convertToEntity(NewCompilationDto newCompilationDto) {
        return modelMapper.map(newCompilationDto, Compilation.class);
    }


}
