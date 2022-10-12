package ru.practicum.explorewithme.compilation;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CompilationStorage {
    Optional<Compilation> addCompilation(Compilation compilation);

    Page<Compilation> getAllCompilations(int from, int size);

    Optional<Compilation> getCompilationById(Long compilationId);

    Optional<Compilation> deleteCompilationById(Long compilationId);

    Optional<Compilation> updateCompilation(Long compilationId, Compilation compilation);
}
