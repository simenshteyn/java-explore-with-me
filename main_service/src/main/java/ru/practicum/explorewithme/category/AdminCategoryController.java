package ru.practicum.explorewithme.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.dto.UpdateCategoryDto;
import ru.practicum.explorewithme.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@Validated
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("")
    public ResponseEntity<?> createCategory(
            HttpServletRequest request,
            @RequestBody @Valid NewCategoryDto newCategoryDto,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(categoryService.createCategory(newCategoryDto));
    }

    @PatchMapping("")
    public ResponseEntity<?> updateCategory(
            HttpServletRequest request,
            @RequestBody @Valid UpdateCategoryDto updateCategoryDto,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(categoryService.updateCategory(updateCategoryDto));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable @Positive Long categoryId) {
        return ResponseEntity.ok(categoryService.removeCategoryById(categoryId));
    }
}
