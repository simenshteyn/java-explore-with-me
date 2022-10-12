package ru.practicum.explorewithme.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.validator.OffsetBasedPageRequest;

import java.util.Optional;

@Component
public class PersistentCategoryStorage implements CategoryStorage {
    private final CategoryRepository categoryRepository;

    @Autowired
    public PersistentCategoryStorage(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> addCategory(Category category) {
        return Optional.of(categoryRepository.save(category));
    }

    @Override
    public Page<Category> getAllCategories(int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    @Transactional
    public Optional<Category> updateCategory(Long categoryId, Category category) {
        Optional<Category> searchCategory = categoryRepository.findById(categoryId);
        if (searchCategory.isEmpty()) return Optional.empty();
        Optional<Category> categoryWithSameName = categoryRepository.findByName(category.getName());
        if (categoryWithSameName.isPresent()) return Optional.empty();
        if (category.getName() != null) searchCategory.get().setName(category.getName());
        return searchCategory;
    }

    @Override
    public Optional<Category> removeCategoryById(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        category.ifPresent(c -> categoryRepository.deleteById(c.getId()));
        return category;
    }
}
