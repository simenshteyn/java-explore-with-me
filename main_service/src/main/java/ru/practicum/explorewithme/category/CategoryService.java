package ru.practicum.explorewithme.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryStorage categoryStorage;

    @Autowired
    public CategoryService(CategoryStorage categoryStorage) {
        this.categoryStorage = categoryStorage;
    }

    public Page<Category> getAllCategories(int from, int size) {
        return categoryStorage.getAllCategories(from, size);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryStorage.getCategoryById(categoryId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find category")
        );
    }

    public Category createCategory(Category category) {
        return categoryStorage.addCategory(category).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add category")
        );
    }

    public Category updateCategory(Long categoryId, Category category) {
        return categoryStorage.updateCategory(categoryId, category).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find category")
        );
    }

    public Category removeCategoryById(Long categoryId) {
        return categoryStorage.removeCategoryById(categoryId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find category")
        );
    }
}
