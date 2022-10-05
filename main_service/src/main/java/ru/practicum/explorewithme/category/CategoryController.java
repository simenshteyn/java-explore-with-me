package ru.practicum.explorewithme.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.validator.OnCreate;
import ru.practicum.explorewithme.validator.OnUpdate;
import ru.practicum.explorewithme.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(categoryService.getAllCategories(from, size));
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable @Positive Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @PostMapping("/admin/categories")
    @Validated(OnCreate.class)
    public ResponseEntity<?> createCategory(
            HttpServletRequest request,
            @RequestBody @Valid Category category,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @PatchMapping("/admin/categories")
    @Validated(OnUpdate.class)
    public ResponseEntity<?> updateCategory(
            HttpServletRequest request,
            @RequestBody @Valid Category category,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(categoryService.updateCategory(category.getId(), category));
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable @Positive Long categoryId) {
        return ResponseEntity.ok(categoryService.removeCategoryById(categoryId));
    }
}
