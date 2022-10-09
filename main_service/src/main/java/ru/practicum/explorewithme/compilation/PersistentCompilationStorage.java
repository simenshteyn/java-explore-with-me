package ru.practicum.explorewithme.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.validator.OffsetBasedPageRequest;

import java.util.Optional;

@Component
public class PersistentCompilationStorage implements CompilationStorage {
    private final CompilationRepository compilationRepository;

    @Autowired
    public PersistentCompilationStorage(CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    @Override
    public Optional<Compilation> addCompilation(Compilation compilation) {
        return Optional.of(compilationRepository.save(compilation));
    }

    @Override
    public Page<Compilation> getAllCompilations(int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        return compilationRepository.findAll(pageable);
    }

    @Override
    public Optional<Compilation> getCompilationById(Long compilationId) {
        return compilationRepository.findById(compilationId);
    }

    @Override
    @Transactional
    public Optional<Compilation> deleteCompilationById(Long compilationId) {
        Optional<Compilation> compilation = compilationRepository.findById(compilationId);
        compilation.ifPresent(c -> compilationRepository.deleteById(c.getId()));
        return compilation;
    }

    @Override
    @Transactional
    public Optional<Compilation> updateCompilation(Long compilationId, Compilation compilation) {
        Compilation searchCompilation = compilationRepository.findById(compilationId).orElse(null);
        if (searchCompilation == null) return Optional.empty();
        if (compilation.getTitle() != null) searchCompilation.setTitle(compilation.getTitle());
        if (compilation.getEvents() != null) searchCompilation.setEvents(compilation.getEvents());
        if (compilation.getPinned() != null) searchCompilation.setPinned(compilation.getPinned());
        return Optional.of(searchCompilation);
    }
}
