package ru.practicum.explorewithme.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.dto.NewUserDto;
import ru.practicum.explorewithme.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public ResponseEntity<?> getUsersByIds(
            @RequestParam(value = "ids") List<Long> ids,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsersByIds(ids, from, size));
    }

    @PostMapping("/admin/users")
    @Validated
    public ResponseEntity<?> createUser(
            HttpServletRequest request,
            @RequestBody @Valid NewUserDto newUserDto,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(userService.createUser(newUserDto));
    }

    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable @Positive Long userId) {
        return ResponseEntity.ok(userService.removeUserById(userId));
    }
}
