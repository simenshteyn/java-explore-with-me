package ru.practicum.explorewithme.category;

import java.util.List;
import java.util.Optional;

public interface CategoryStorage {
    Optional<Category> addCategory(Category category);
    List<Category> getAllCategories(int from, int size);
    Optional<Category> getCategoryById(Long categoryId);
    Optional<Category> updateCategory(Long categoryId, Category category);
    Optional<Category> removeCategoryById(Long categoryId);
}
