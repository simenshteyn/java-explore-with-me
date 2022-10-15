package ru.practicum.explorewithme.category;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.dto.UpdateCategoryDto;


@Service
public class CategoryService {
    private final CategoryStorage categoryStorage;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryService(CategoryStorage categoryStorage, ModelMapper modelMapper) {
        this.categoryStorage = categoryStorage;
        this.modelMapper = modelMapper;
    }

    public Page<Category> getAllCategories(int from, int size) {
        return categoryStorage.getAllCategories(from, size);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryStorage.getCategoryById(categoryId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find category")
        );
    }

    public Category createCategory(NewCategoryDto newCategoryDto) {
        Category category = modelMapper.map(newCategoryDto, Category.class);
        return categoryStorage.addCategory(category).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add category")
        );
    }

    public Category updateCategory(UpdateCategoryDto updateCategoryDto) {
        Category category = modelMapper.map(updateCategoryDto, Category.class);
        return categoryStorage.updateCategory(updateCategoryDto.getId(), category).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find category")
        );
    }

    public Category removeCategoryById(Long categoryId) {
        return categoryStorage.removeCategoryById(categoryId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find category")
        );
    }
}
