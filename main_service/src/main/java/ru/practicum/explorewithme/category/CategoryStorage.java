package ru.practicum.explorewithme.category;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CategoryStorage {
    Optional<Category> addCategory(Category category);

    Page<Category> getAllCategories(int from, int size);

    Optional<Category> getCategoryById(Long categoryId);

    Optional<Category> updateCategory(Long categoryId, Category category);

    Optional<Category> removeCategoryById(Long categoryId);
}
